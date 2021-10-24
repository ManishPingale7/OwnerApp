package com.example.ownerapp.Adapters

data class Cart(
    val Product: String,
    val Quantity: Int,
) {
    constructor() : this(" ", 0)
}