package com.example.ownerapp.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ownerapp.Adapters.ProductsAdapter
import com.example.ownerapp.R
import com.example.ownerapp.data.ProductCategory
import com.example.ownerapp.databinding.ActivityViewCategoryProductsBinding
import com.example.ownerapp.di.components.DaggerFactoryComponent
import com.example.ownerapp.di.modules.FactoryModule
import com.example.ownerapp.di.modules.RepositoryModule
import com.example.ownerapp.mvvm.repository.MainRepository
import com.example.ownerapp.mvvm.viewmodles.MainViewModel

class ViewCategoryProducts : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var component: DaggerFactoryComponent
    lateinit var binding: ActivityViewCategoryProductsBinding
    private lateinit var productsAdapter: ProductsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewCategoryProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        loadData()
        binding.goBackProductsCat.setOnClickListener {
            finish()
        }
//        TODO("CHANGE THE WAY VIEW MODEL IS ADDED")
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


        //Setting the recycler view
        productsAdapter = ProductsAdapter(this)

        binding.apply {
            recyclerViewProducts.apply {
                adapter = productsAdapter
                layoutManager = LinearLayoutManager(this@ViewCategoryProducts)
                setHasFixedSize(true)
            }
        }

    }


    @SuppressLint("NotifyDataSetChanged")
    private fun loadData() {
        val category = intent.getParcelableExtra<ProductCategory>("Category")
        category?.let {
            viewModel.loadProducts(category.name).observe(this) {
                productsAdapter.submitList(it)
                productsAdapter.notifyDataSetChanged()
            }
        }
    }
}