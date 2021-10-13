package com.example.ownerapp.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
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
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText


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

                val productName =
                    bottomSheetView.findViewById<TextInputEditText>(R.id.bottomSheetName)
                val productDesc =
                    bottomSheetView.findViewById<TextInputEditText>(R.id.bottomSheetDesc)
                val productPrice =
                    bottomSheetView.findViewById<EditText>(R.id.bottomSheetProductPrice)
                val saveBtn = bottomSheetView.findViewById<MaterialButton>(R.id.saveProductBtn)
                val deleteBtn = bottomSheetView.findViewById<AppCompatButton>(R.id.deleteBtn)

                productName.setText(product.name)
                productDesc.setText(product.desc)
                productPrice.setText(product.price)

                deleteBtn.setOnClickListener {
                    MaterialAlertDialogBuilder(
                        this@ViewCategoryProducts,
                        R.style.ThemeOverlay_AppCompat_Dialog
                    )
                        .setMessage(resources.getString(R.string.productdeletemsg))
                        .setNegativeButton(resources.getString(R.string.decline)) { dialog, which ->
                            dialog.dismiss()
                        }
                        .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                            Toast.makeText(this@ViewCategoryProducts, "Clicked", Toast.LENGTH_SHORT)
                                .show()
                            viewModel.deleteProduct(product)
                            dialog.dismiss()
                            bottomSheetDialog.dismiss()
                        }
                        .show()


                }
                saveBtn.setOnClickListener {
                    val desc = productDesc.text.toString()
                    val name = productName.text.toString()
                    val price = productPrice.text.toString()
                    if (desc != product.desc || name != product.name || price != product.price) {

                        MaterialAlertDialogBuilder(
                            this@ViewCategoryProducts,
                            R.style.ThemeOverlay_AppCompat_Dialog
                        )
                            .setMessage(resources.getString(R.string.productupdatemsg))
                            .setNegativeButton(resources.getString(R.string.decline)) { dialog, which ->
                                dialog.dismiss()
                            }
                            .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                                product.name=name
                                product.desc=desc
                                product.price=price
                                viewModel.updateProduct(product)
                            }
                            .show()


                        Toast.makeText(
                            this@ViewCategoryProducts,
                            "Product Changed",
                            Toast.LENGTH_SHORT
                        ).show()


                    } else {
                        Toast.makeText(this@ViewCategoryProducts, "Not Changed", Toast.LENGTH_SHORT)
                            .show()
                    }
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