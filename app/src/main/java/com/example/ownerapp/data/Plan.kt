package com.example.ownerapp.data

data class Plan(
    var name: String,
    var desc: String,
    var timeNumber: String,
    var timetype: String,
    var fees: String
) {
    constructor() : this("", "", "", "", "")
}