package com.example.ownerapp.data

data class ProductCategory(
    var name: String,
    var totalProducts: String
) {
    constructor() : this("", "")
}
