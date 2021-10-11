package com.example.ownerapp.data

import java.util.*

data class Product(
    var name: String,
    var desc: String,
    var price: String,
    var category: String,
    var productImage: String,
    var flavours: ArrayList<String>? = null,
    var key: String? = null
) {
    constructor() : this("", "", "", "", "", ArrayList<String>(), "")
}