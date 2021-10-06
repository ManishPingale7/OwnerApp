package com.example.ownerapp.data

data class ProductCategory(
    val name: String,
    val image: String,
) {
    constructor() : this("", "")
}
