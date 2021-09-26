package com.example.ownerapp.data

import android.net.Uri
import java.util.*

data class Product(
    var name: String,
    var desc: String,
    var price: String,
    var category: String,
    var productImages: ArrayList<String>,
    var key:String?=null
) {
    constructor() : this("", "", "", "", ArrayList<String>(),"")
}