package com.example.ownerapp.mvvm.repository

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log

class AuthRepository(var context: Context) : BaseRepository(context) {

    fun login(email: String, password: String) {
        Log.d(TAG, "login: $email and $password")

    }



}