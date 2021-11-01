package com.example.ownerapp.data

data class Cart(
    val Product: String,
    val Quantity: Int,
) {
    constructor() : this(" ", 0)
}