package com.example.ownerapp.data

data class Cart(
    val Product: String,
    val Quantity: Int,
    val Customer: String,
    val status: String
) {
    constructor() : this(" ", 0, "", "")
}