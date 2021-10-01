package com.example.ownerapp.data

import android.net.Uri

data class ProductCategory(
    var name: String,
    var image:String
) {
    constructor() : this("","")
}
