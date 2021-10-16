package com.example.ownerapp.data

data class Plan(
    val name: String,
    val desc: String,
    val timeNumber: String,
    val fees: String,
    val pt: Boolean?,
    var key: String,

    val totalDays: Int
) {
    constructor() : this("", "", "", "",null," ",-1)
}