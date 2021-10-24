package com.example.ownerapp.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.ownerapp.data.Product
import com.example.ownerapp.databinding.CartitemBinding
import com.google.gson.Gson

class CartAdapter(val context: Context, private val dataSet: ArrayList<Cart>) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    private val gson = Gson()

    inner class CartViewHolder(val binding: CartitemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cart: Cart) {
            binding.apply {
                Log.d("TAG", "bind: $cart")
                val product = gson.fromJson(cart.Product, Product::class.java)
                val text = "₹ ${product.price}"
                productNameCard.text = product.name
                productPrice.text = text
                bottomQuantityTextView.text = cart.Quantity.toString()
                Glide.with(context)
                    .load(product.productImage)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .fitCenter()
                    .into(productImage)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        return CartViewHolder(
            CartitemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(dataSet[position])
    }

    override fun getItemCount() = dataSet.size
}