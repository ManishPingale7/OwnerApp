package com.example.ownerapp.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.ownerapp.data.Cart
import com.example.ownerapp.data.Product
import com.example.ownerapp.databinding.OrderItemBinding
import com.google.gson.Gson

class CartAdapter(private val context: Context) :
    ListAdapter<Cart, CartAdapter.ViewHolder>(DiffCallBack1()) {
    private val gson = Gson()
    private lateinit var mListener: buttonListeners

    interface buttonListeners {
        fun onAcceptListener(cart: Cart)
        fun onDeleteListener(cart: Cart)
    }

    fun setListener(listener: buttonListeners) {
        mListener = listener
    }

    inner class ViewHolder(private val binding: OrderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cart: Cart) {
            binding.apply {
                if (adapterPosition == 0)
                    topStrip.visibility = View.VISIBLE
                val product = gson.fromJson(cart.Product, Product::class.java)
                val text = "₹ ${product.price}"
                productNameCard.text = product.name
                productPrice.text = text
                productQuantityCard.text = cart.Quantity.toString()
                Glide.with(context)
                    .load(product.productImage)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .fitCenter()
                    .into(productImage)

                acceptButton.setOnClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION)
                        mListener.onAcceptListener(getItem(adapterPosition))
                }

                deleteButton.setOnClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION)
                        mListener.onDeleteListener(getItem(adapterPosition))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            OrderItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

private class DiffCallBack1 : DiffUtil.ItemCallback<Cart>() {
    override fun areItemsTheSame(oldItem: Cart, newItem: Cart): Boolean {
        TODO("Not yet implemented")
    }

    override fun areContentsTheSame(oldItem: Cart, newItem: Cart): Boolean {
        TODO("Not yet implemented")
    }

}