package com.example.ownerapp.activities

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.GetMultipleContents
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.ebanx.swipebtn.OnStateChangeListener
import com.example.ownerapp.Adapters.ImageSliderAdapter
import com.example.ownerapp.R
import com.example.ownerapp.data.Product
import com.example.ownerapp.data.SliderItem
import com.example.ownerapp.databinding.ActivityAddNewProductBinding
import com.example.ownerapp.di.components.DaggerFactoryComponent
import com.example.ownerapp.di.modules.FactoryModule
import com.example.ownerapp.di.modules.RepositoryModule
import com.example.ownerapp.mvvm.repository.MainRepository
import com.example.ownerapp.mvvm.viewmodles.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController.ClickListener
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView


class AddNewProduct : AppCompatActivity() {
    lateinit var binding: ActivityAddNewProductBinding
    var arrayListImages = ArrayList<String>()
    private lateinit var mAuth: FirebaseAuth
    private lateinit var viewModel: MainViewModel
    private lateinit var component: DaggerFactoryComponent
    var position = 0
    private var adapter: ImageSliderAdapter? = null

    var getContent = registerForActivityResult(GetMultipleContents()) { it ->
        binding.ImageLay.visibility = View.VISIBLE

        arrayListImages.clear()
        it.forEach {
            it?.let { it1 ->
                arrayListImages.add(it1.toString())
                Log.d(TAG, "Selected Images are$it1: ")
            }

        }
        applySelectedPhotos(binding.sliderView)
        Log.d(TAG, "Selected Images are ${arrayListImages.size}: ")
        position = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        initSlider()

        binding.sliderView.setOnIndicatorClickListener(ClickListener {
            Log.i(
                "GGG",
                "onIndicatorClicked: " + binding.sliderView.currentPagePosition
            )
        })



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

        binding.submitProduct.setOnStateChangeListener(OnStateChangeListener {
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
                                val product = Product(name, desc, price, category, arrayListImages,"")
                                viewModel.addProduct(product)
                                Intent(this, MainActivity::class.java).also {
                                    startActivity(it)
                                    finish()
                                }
                            } else
                                Toast.makeText(this, "Select Product Images", Toast.LENGTH_SHORT)
                                    .show()

                        } else
                            Toast.makeText(this, "Enter Description", Toast.LENGTH_SHORT).show()

                    } else
                        Toast.makeText(this, "Select The Category", Toast.LENGTH_SHORT).show()

                } else
                    Toast.makeText(this, "Enter Product Price", Toast.LENGTH_SHORT).show()

            } else
                Toast.makeText(this, "Enter Product name", Toast.LENGTH_SHORT).show()

        })


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


    private fun initSlider() {
        adapter = ImageSliderAdapter()
        adapter!!.setContext(this)
        binding.sliderView.setSliderAdapter(adapter!!)
        binding.sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM)//set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        binding.sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        binding.sliderView.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH
        binding.sliderView.indicatorSelectedColor = Color.WHITE
        binding.sliderView.indicatorUnselectedColor = Color.GRAY
        binding.sliderView.scrollTimeInSec = 3
        binding.sliderView.isAutoCycle = true
        binding.sliderView.startAutoCycle()
    }


    fun applySelectedPhotos(view: View?) {
        val sliderItemList: MutableList<SliderItem> = ArrayList()
        //dummy data
        for (i in 0 until arrayListImages.size) {
            val sliderItem = SliderItem()
            sliderItem.description = "Slider Item $i"
            sliderItem.imageUrl = arrayListImages[i].toString()
            sliderItemList.add(sliderItem)
        }
        adapter!!.renewItems(sliderItemList as ArrayList<SliderItem>)
    }

//    fun removeLastItem(view: View?) {
//        if (adapter!!.count - 1 >= 0) adapter!!.deleteItem(adapter!!.count - 1)
//    }
//
//    fun addNewItem(view: View?) {
//        val sliderItem = SliderItem()
//        sliderItem.description = "Slider Item Added Manually"
//        sliderItem.imageUrl =
//            "https://images.pexels.com/photos/929778/pexels-photo-929778.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260"
//        adapter!!.addItem(sliderItem)
//    }
}