package com.example.ownerapp.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.ownerapp.R
import com.example.ownerapp.Utils.ProgressBtn
import com.example.ownerapp.data.ProductCategory
import com.example.ownerapp.databinding.ActivityAddNewCategoryBinding
import com.example.ownerapp.di.components.DaggerFactoryComponent
import com.example.ownerapp.di.modules.FactoryModule
import com.example.ownerapp.di.modules.RepositoryModule
import com.example.ownerapp.mvvm.repository.MainRepository
import com.example.ownerapp.mvvm.viewmodles.MainViewModel

class AddNewCategory : AppCompatActivity() {

    var imageUri: Uri? = null

    var getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { it ->

        Glide.with(this)
            .load(it)
            .fitCenter()
            .into(binding.pickImage)
        imageUri=it
    }


    lateinit var binding: ActivityAddNewCategoryBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var component: DaggerFactoryComponent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

        binding.pickImage.setOnClickListener {
            getContent.launch("image/*")
        }

        val view = findViewById<View>(R.id.submitCat2)

        view.setOnClickListener {
            val progressBtn=ProgressBtn(this,view)
            val name = binding.categoryName.text.toString()
            if (name.isNotEmpty()) {
                if (imageUri != null) {
                    progressBtn.buttonActivated()
                    val cat = ProductCategory(name, imageUri.toString())
                    viewModel.addCategory(cat)
                    Handler(Looper.getMainLooper()).postDelayed({
                        progressBtn.buttonfinished()
                        Intent(this, MainActivity::class.java).also {
                            startActivity(it)
                            finish()
                        }
                    }, 3000)
                } else {
                    Toast.makeText(this, "Select Image", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Enter Category Name", Toast.LENGTH_SHORT).show()
            }
        }

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