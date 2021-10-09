package com.example.ownerapp.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.ownerapp.data.Product
import com.example.ownerapp.databinding.ProductlistitemBinding

class ProductsAdapter(val context: Context) :
    ListAdapter<Product, ProductsAdapter.viewHolder>(DiffCallBack()) {

//    private var context: Context? = null
//    fun setContext(context: Context) {
//        this.context = context
//    }


    inner class viewHolder(private val binding: ProductlistitemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            Log.d("TAG", "bind: BINDING THIS -$product")
            binding.productNameCard.text = product.name
            binding.productPrice.text = product.price
            Log.d("TAG", "bind:${product.productImages[0]} ")
            Glide.with(context)
                .load(product.productImages[0])
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .fitCenter()
                .into(binding.productImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        Log.d("TAG", "onCreateViewHolder: INNNNNNNNNNNNNNNNNNNNNNNNN")
        return viewHolder(
            ProductlistitemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallBack : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product) =
            oldItem.key == newItem.key

        override fun areContentsTheSame(oldItem: Product, newItem: Product) = oldItem == newItem

    }
}