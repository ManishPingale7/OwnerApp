package com.example.ownerapp.Adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.ownerapp.R
import com.example.ownerapp.data.ProductCategory


class GridAdapter(
    var requireContext: Context,
    var it: ArrayList<ProductCategory>

) : BaseAdapter() {

    var context: Context? = requireContext

    var inflater: LayoutInflater? = null

    override fun getCount(): Int {
        return it.size
    }

    override fun getItem(position: Int): Any? {
        return it[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView: View? = convertView
        if (inflater == null) inflater =
            context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?
        if (convertView == null) {
            convertView = inflater!!.inflate(R.layout.grid_item, null)
        }
        val imageView: ImageView = convertView!!.findViewById(R.id.grid_image)
        val textView: TextView = convertView.findViewById(R.id.item_name)
        Glide.with(convertView)
            .load(it[position].image)
            .fitCenter()
            .into(imageView)

        textView.text = it[position].name
        return convertView
    }


}