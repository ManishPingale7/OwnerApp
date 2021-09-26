package com.example.ownerapp.activities

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.ebanx.swipebtn.OnStateChangeListener
import com.example.ownerapp.R
import com.example.ownerapp.databinding.ActivityAddNewCategoryBinding
import com.example.ownerapp.di.components.DaggerFactoryComponent
import com.example.ownerapp.di.modules.FactoryModule
import com.example.ownerapp.di.modules.RepositoryModule
import com.example.ownerapp.mvvm.repository.MainRepository
import com.example.ownerapp.mvvm.viewmodles.MainViewModel

class AddNewCategory : AppCompatActivity() {
    lateinit var binding: ActivityAddNewCategoryBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var component: DaggerFactoryComponent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        binding.swipeBtnAddCategory.setOnStateChangeListener(OnStateChangeListener {
            Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show()
        })

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


    }
}