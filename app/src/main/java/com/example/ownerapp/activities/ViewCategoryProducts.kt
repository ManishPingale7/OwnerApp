package com.example.ownerapp.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ownerapp.Adapters.ProductsAdapter
import com.example.ownerapp.R
import com.example.ownerapp.data.Product
import com.example.ownerapp.data.ProductCategory
import com.example.ownerapp.databinding.ActivityViewCategoryProductsBinding
import com.example.ownerapp.di.components.DaggerFactoryComponent
import com.example.ownerapp.di.modules.FactoryModule
import com.example.ownerapp.di.modules.RepositoryModule
import com.example.ownerapp.mvvm.repository.MainRepository
import com.example.ownerapp.mvvm.viewmodles.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog



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

    }


    private fun init() {
        //Toolbar stuff
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


        //Setting the recycler view
        productsAdapter = ProductsAdapter(this)
        productsAdapter.setOnEditClickListener(object : ProductsAdapter.onItemClickedListener {
            override fun onEditButtonClicked(product: Product) {

                val bottomSheetDialog =
                    BottomSheetDialog(this@ViewCategoryProducts, R.style.BottomSheetDialogTheme)

                val bottomSheetView = LayoutInflater.from(this@ViewCategoryProducts).inflate(
                    R.layout.layout_bottom_sheet,
                    this@ViewCategoryProducts.findViewById(R.id.bottom_sheet_Container)
                )

                bottomSheetDialog.setCanceledOnTouchOutside(false)
                bottomSheetDialog.setContentView(bottomSheetView)
                bottomSheetDialog.show()

                val productName = bottomSheetView.findViewById<TextView>(R.id.bottomSheetName)
                val productDesc = bottomSheetView.findViewById<TextView>(R.id.bottomSheetDesc)
                val productPrice =
                    bottomSheetView.findViewById<EditText>(R.id.bottomSheetProductPrice)
                val saveBtn = bottomSheetView.findViewById<Button>(R.id.saveProductBtn)
                val deleteBtn = bottomSheetView.findViewById<Button>(R.id.deleteBtn)

                productName.text = product.name
                productDesc.text = product.desc
                productPrice.setText(product.price)

                saveBtn.setOnClickListener {
                    val desc = productDesc.text.toString()
                    val name = productName.text.toString()
                    val price = productPrice.text.toString()


                    if (desc != product.desc || name != product.name || price != product.price) {
//                        var newProd=Product(name,desc,)
                    }
                }

                deleteBtn.setOnClickListener {

                }
            }
        })

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