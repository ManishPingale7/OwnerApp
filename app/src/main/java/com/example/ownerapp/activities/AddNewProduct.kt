package com.example.ownerapp.activities

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.GetMultipleContents
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.example.ownerapp.Adapters.ImageSliderAdapter
import com.example.ownerapp.Utils.ProgressBtn
import com.example.ownerapp.data.Product
import com.example.ownerapp.data.SliderItem
import com.example.ownerapp.databinding.ActivityAddNewProductBinding
import com.example.ownerapp.di.components.DaggerFactoryComponent
import com.example.ownerapp.di.modules.FactoryModule
import com.example.ownerapp.di.modules.RepositoryModule
import com.example.ownerapp.mvvm.repository.MainRepository
import com.example.ownerapp.mvvm.viewmodles.MainViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.auth.FirebaseAuth
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView


class AddNewProduct : AppCompatActivity() {
    lateinit var binding: ActivityAddNewProductBinding
    var arrayListImages = ArrayList<String>()
    private lateinit var mAuth: FirebaseAuth
    private lateinit var viewModel: MainViewModel
    private lateinit var component: DaggerFactoryComponent
    private var position = 0
    private var categories = ArrayList<String>()
    private var flavoursChips = ArrayList<String>()
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
        applySelectedPhotos()
        Log.d(TAG, "Selected Images are ${arrayListImages.size}: ")
        position = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()


        binding.switchflavour.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Log.d(TAG, "onCreate: Checked")
                binding.flavoursLay.visibility = View.VISIBLE
            } else {
                binding.flavoursLay.visibility = View.GONE
                Log.d(TAG, "onCreate: Not Checked")

            }
        }




        binding.addFlavour.setOnClickListener {
            val txtVal = binding.flavouredit.text
            if (!txtVal.isNullOrEmpty()) {
                addChipToGroup(txtVal.toString(), binding.chipGroup2)
                binding.flavouredit.setText("")
            }
        }




        viewModel.repository.fetchAllCategoriesNames().observe(this) {
            val arrayAdapter = ArrayAdapter(
                this, com.example.ownerapp.R.layout.dropdownitem,
                it.toArray()

            )
            categories = it
            arrayAdapter.notifyDataSetChanged()
            binding.productCategory.setAdapter(arrayAdapter)
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

        val view = findViewById<View>(com.example.ownerapp.R.id.submitProduct2)

        view.setOnClickListener {
            val progressBtn = ProgressBtn(this, view)
            val name = binding.productName.text.toString()
            val price = binding.productPrice.text.toString()
            val category = binding.productCategory.text.toString()
            val desc = binding.productDesc.text.toString()

            addChipsToArray(binding.chipGroup2)


            if (name.isNotEmpty()) {
                if (price.isNotEmpty()) {
                    if (category.isNotEmpty() && categories.contains(category)) {
                        if (desc.isNotEmpty()) {
                            if (arrayListImages.size > 0) {
                                //Adding Products here
                                progressBtn.buttonActivated()
                                val product = Product(name, desc, price, category, arrayListImages,flavoursChips)
                                viewModel.addProduct(product)
                                Handler(Looper.getMainLooper()).postDelayed({
                                    progressBtn.buttonfinished()
                                    Intent(this@AddNewProduct, MainActivity::class.java).also {
                                        startActivity(it)
                                        finish()
                                    }
                                }, 3000)


                            } else {
                                Toast.makeText(this, "Select Product Images", Toast.LENGTH_SHORT)
                                    .show()

                            }
                        } else {
                            Toast.makeText(this, "Enter Description", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Enter Valid Category", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Enter Product Price", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Enter Product name", Toast.LENGTH_SHORT).show()
            }
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
        window.statusBarColor =
            ContextCompat.getColor(this, com.example.ownerapp.R.color.colorprimarymain)

        component = DaggerFactoryComponent.builder()
            .repositoryModule(RepositoryModule(this))
            .factoryModule(FactoryModule(MainRepository(this)))
            .build() as DaggerFactoryComponent
        viewModel = ViewModelProviders.of(this, component.getFactory())
            .get(MainViewModel::class.java)
        mAuth = FirebaseAuth.getInstance()


        initSlider()

    }


    private fun addChipToGroup(txt: String, chipGroup: ChipGroup) {
        val chip = Chip(this)
        chip.text = txt
        chip.isCloseIconEnabled = true
        chip.setChipIconTintResource(android.R.color.holo_blue_light)
        chip.isClickable = false
        chip.isCheckable = false
        chipGroup.addView(chip as View)
        chip.setOnCloseIconClickListener { chipGroup.removeView(chip as View) }
        printChipsValue(chipGroup)
    }

    private fun printChipsValue(chipGroup: ChipGroup) {
        for (i in 0 until chipGroup.childCount) {
            val chipObj = chipGroup.getChildAt(i) as Chip
        }
    }

    private fun addChipsToArray(chipGroup: ChipGroup) {
        flavoursChips.clear()
        for (i in 0 until chipGroup.childCount) {
            val chipObj = chipGroup.getChildAt(i) as Chip
            flavoursChips.add(chipObj.text.toString())
        }
    }


    private fun applySelectedPhotos() {
        val sliderItemList: MutableList<SliderItem> = ArrayList()
        for (i in 0 until arrayListImages.size) {
            val sliderItem = SliderItem()
            sliderItem.description = "Slider Item $i"
            sliderItem.imageUrl = arrayListImages[i]
            sliderItemList.add(sliderItem)
        }
        adapter!!.renewItems(sliderItemList as ArrayList<SliderItem>)
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

}