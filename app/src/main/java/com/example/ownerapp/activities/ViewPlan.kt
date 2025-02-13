package com.example.ownerapp.activities

import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ownerapp.Adapters.ViewPlansAdapter
import com.example.ownerapp.R
import com.example.ownerapp.databinding.ActivityViewPlanBinding
import com.example.ownerapp.di.components.DaggerFactoryComponent
import com.example.ownerapp.di.modules.FactoryModule
import com.example.ownerapp.di.modules.RepositoryModule
import com.example.ownerapp.mvvm.repository.MainRepository
import com.example.ownerapp.mvvm.viewmodles.MainViewModel

class ViewPlan : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var component: DaggerFactoryComponent
    lateinit var binding: ActivityViewPlanBinding
    private var viewPlansAdapter = ViewPlansAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            recyclerViewPlans.apply {
                adapter = viewPlansAdapter
                layoutManager = LinearLayoutManager(this@ViewPlan)
                setHasFixedSize(true)
            }
        }

        init()
        setupRecycler()

    }

    private fun setupRecycler() {

        viewModel.getAllPlans().observe(this) {
            viewPlansAdapter.submitList(it)
            Log.d("TAG", "init:$it ")
            viewPlansAdapter.notifyDataSetChanged()
        }
    }

    private fun init() {
        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorprimarymain)
        component = DaggerFactoryComponent.builder()
            .repositoryModule(RepositoryModule(this))
            .factoryModule(FactoryModule(MainRepository(this)))
            .build() as DaggerFactoryComponent
        viewModel = ViewModelProviders.of(this, component.getFactory())
            .get(MainViewModel::class.java)


    }
}