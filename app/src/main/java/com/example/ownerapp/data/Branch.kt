package com.example.ownerapp.data

data class Branch(
    val branchName: String,
    val branchID: String,
    val branchPass: String
){
    constructor() : this("","","") {

    }
}
