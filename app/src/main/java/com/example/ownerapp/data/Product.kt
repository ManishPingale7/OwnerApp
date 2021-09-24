package com.example.ownerapp.data

import android.net.Uri
import java.util.*

data class Product(
    var name: String,
    var desc: String,
    var price: String,
    var category: String,
    var productImages: ArrayList<Uri>
) {
    constructor() : this("", "", "", "", ArrayList<Uri>())
}