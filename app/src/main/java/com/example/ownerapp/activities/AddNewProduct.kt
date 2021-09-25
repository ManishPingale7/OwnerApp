package com.example.ownerapp.activities

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.GetMultipleContents
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.example.ownerapp.R
import com.example.ownerapp.data.Product
import com.example.ownerapp.databinding.ActivityAddNewProductBinding
import com.example.ownerapp.di.components.DaggerFactoryComponent
import com.example.ownerapp.di.modules.FactoryModule
import com.example.ownerapp.di.modules.RepositoryModule
import com.example.ownerapp.mvvm.repository.MainRepository
import com.example.ownerapp.mvvm.viewmodles.MainViewModel
import com.google.firebase.auth.FirebaseAuth


class AddNewProduct : AppCompatActivity() {
    lateinit var binding: ActivityAddNewProductBinding
    var arrayListImages = ArrayList<Uri>()
    private lateinit var mAuth: FirebaseAuth
    private lateinit var viewModel: MainViewModel
    private lateinit var component: DaggerFactoryComponent
    var position = 0


    var getContent = registerForActivityResult(GetMultipleContents()) { it ->
        binding.ImageLay.visibility = View.VISIBLE

        arrayListImages.clear()
        it.forEach {
            it?.let { it1 ->
                arrayListImages.add(it1)
                Log.d(TAG, "Selected Images are$it1: ")
            }

        }
        Log.d(TAG, "Selected Images are ${arrayListImages.size}: ")
        binding.imageSwitcherAdd.setImageURI(arrayListImages[0])
        binding.imageSwitcherAdd.visibility = View.VISIBLE
        position = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

        binding.imageSwitcherAdd.setFactory {
            val imageView = ImageView(applicationContext)
            imageView
        }
        binding.pickImages.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                pickImage()

            } else {
                checkForPermissions()
            }
        }

        binding.submitProduct.setOnClickListener {
            val name = binding.productName.text.toString()
            val price = binding.productPrice.text.toString()
            val category = binding.productCategory.text.toString()
            val desc = binding.productDesc.text.toString()


            if (name.isNotEmpty()) {
                if (price.isNotEmpty()) {
                    if (category.isNotEmpty()) {
                        if (desc.isNotEmpty()) {
                            if (arrayListImages.size > 0) {
                                //Adding Products here
                                val product = Product(name, desc, price, category, arrayListImages)
                                viewModel.addProduct(product)
                                Intent(this, MainActivity::class.java).also {
                                    startActivity(it)
                                    finish()
                                }
                            } else {
                                Toast.makeText(this, "Select Product Images", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        } else {
                            Toast.makeText(this, "Enter Description", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Select The Category", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Enter Product Price", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Enter Product name", Toast.LENGTH_SHORT).show()
            }

        }


        binding.backButton.setOnClickListener {
            Log.d(TAG, "Back $position size = ${arrayListImages.size}")
            if (position > 0) {
                position--
                binding.imageSwitcherAdd.setImageURI(arrayListImages[position])
            }
        }

        binding.frontButton.setOnClickListener {
            Log.d(TAG, "Front $position")
            if (position < arrayListImages.size) {
                binding.imageSwitcherAdd.setImageURI(arrayListImages[position])
                position++
            }
        }

        binding.submitProduct.setOnClickListener {

        }
    }

    private fun checkForPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED -> {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                    showPerDialog()
                }

                else -> {
                    ActivityCompat.requestPermissions(
                        this, arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ), 222
                    )
                }
            }
        }
    }

    private fun showPerDialog() {
        val builder = AlertDialog.Builder(this)
        builder.apply {
            setMessage("Permission to Product Photos")
            setTitle("Photo Permission")
            setPositiveButton("ok") { _, _ ->
                ActivityCompat.requestPermissions(
                    this@AddNewProduct,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    222
                )
            }

        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun pickImage() {
        getContent.launch("image/*")
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        fun innerCheck() {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Not Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()

            }
        }

        when (requestCode) {
            202 -> {
                innerCheck()
            }
        }

    }

    private fun init() {
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
        mAuth = FirebaseAuth.getInstance()

    }


}