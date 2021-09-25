package com.example.ownerapp.Adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.ownerapp.R
import com.example.ownerapp.data.SliderItem
import com.smarteist.autoimageslider.SliderViewAdapter

class ImageSliderAdapter : SliderViewAdapter<ImageSliderAdapter.SliderAdapterVH>() {

    private var context: Context? = null
    private var mSliderItems: ArrayList<SliderItem> = ArrayList()

    fun setContext(context: Context?) {
        this.context = context
    }

    fun renewItems(sliderItems: ArrayList<SliderItem>) {
        mSliderItems = sliderItems
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        mSliderItems.removeAt(position)
        notifyDataSetChanged()
    }

    fun addItem(sliderItem: SliderItem?) {
        mSliderItems.add(sliderItem!!)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterVH? {
        val inflate: View =
            LayoutInflater.from(parent.context).inflate(R.layout.image_slider_layout_item, null)
        return SliderAdapterVH(inflate)
    }


    override fun onBindViewHolder(viewHolder: SliderAdapterVH, position: Int) {
        val sliderItem: SliderItem = mSliderItems[position]
        viewHolder.textViewDescription.text = sliderItem.description
        viewHolder.textViewDescription.textSize = 16F
        viewHolder.textViewDescription.setTextColor(Color.WHITE)
        Glide.with(viewHolder.itemView)
            .load(sliderItem.imageUrl)
            .fitCenter()
            .into(viewHolder.imageViewBackground)
        viewHolder.itemView.setOnClickListener(View.OnClickListener {
            Toast.makeText(
                context,
                "This is item in position $position",
                Toast.LENGTH_SHORT
            ).show()
        })
    }

    override fun getCount(): Int {
        //slider view count could be dynamic size
        return mSliderItems.size
    }

    class SliderAdapterVH(itemView: View) :
        ViewHolder(itemView) {
        var itemViewThis: View? = null
        var imageViewBackground: ImageView = itemView.findViewById(R.id.iv_auto_image_slider)
        var imageGifContainer: ImageView = itemView.findViewById(R.id.iv_gif_container)
        var textViewDescription: TextView = itemView.findViewById(R.id.tv_auto_image_slider)

        init {
            itemViewThis = itemView
        }
    }
}