package com.example.ownerapp.activities

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ownerapp.Adapters.BranchesAdapter
import com.example.ownerapp.R
import com.example.ownerapp.databinding.ActivityViewAllBranchesBinding
import com.example.ownerapp.di.components.DaggerFactoryComponent
import com.example.ownerapp.di.modules.FactoryModule
import com.example.ownerapp.di.modules.RepositoryModule
import com.example.ownerapp.mvvm.repository.MainRepository
import com.example.ownerapp.mvvm.viewmodles.MainViewModel

class ViewAllBranches : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private val branchesAdapter = BranchesAdapter()
    private lateinit var component: DaggerFactoryComponent
    lateinit var binding: ActivityViewAllBranchesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewAllBranchesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            recyclerViewBranches.apply {
                adapter = branchesAdapter
                layoutManager = LinearLayoutManager(this@ViewAllBranches)
                setHasFixedSize(true)
            }
        }
        init()


    }

    private fun init() {
        //Toolbar stuff
        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.my_statusbar_color)

        component = DaggerFactoryComponent.builder()
            .repositoryModule(RepositoryModule(this))
            .factoryModule(FactoryModule(MainRepository(this)))
            .build() as DaggerFactoryComponent
        viewModel = ViewModelProviders.of(this, component.getFactory())
            .get(MainViewModel::class.java)

        //RecyclerView Stuff
        branchesAdapter.submitList(viewModel.branches)
    }
}