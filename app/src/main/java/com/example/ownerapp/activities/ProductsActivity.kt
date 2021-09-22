package com.example.ownerapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.ownerapp.R
import com.example.ownerapp.databinding.ActivityProductsBinding

class ProductsActivity : AppCompatActivity() {
    lateinit var binding: ActivityProductsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarProds)
        // enabling action bar app icon and behaving it as toggle button
//        actionBar?.setDisplayHomeAsUpEnabled(true)
//        actionBar?.setHomeButtonEnabled(true)
        val navController = findNavController(R.id.ContainerViewProducts)
        val appBarConfig = AppBarConfiguration(setOf(R.id.products, R.id.orders))

        setupActionBarWithNavController(navController, appBarConfig)
        binding.bottomNavigationProds.setupWithNavController(navController)

    }
}