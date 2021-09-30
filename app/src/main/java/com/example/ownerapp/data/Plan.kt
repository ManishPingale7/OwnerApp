package com.example.ownerapp.data

data class Plan(
    val name: String,
    val desc: String,
    val timeNumber: String,
    val fees: String,
    val pt:Boolean?
) {
    constructor() : this("", "", "", "",null)
}