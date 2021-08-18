package com.example.ownerapp.mvvm.repository

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log

import android.content.Intent

import android.widget.Toast
import com.example.ownerapp.activities.LoginActivity
import com.example.ownerapp.activities.MainActivity
import com.google.firebase.auth.FirebaseAuth


class AuthRepository(var context: Context) : BaseRepository(context) {

    private var mAuth = FirebaseAuth.getInstance()

    fun login(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (mAuth.currentUser!!.isEmailVerified) {
                            Toast.makeText(context, "Signed In as $email", Toast.LENGTH_SHORT)
                                .show()
                            Intent(context, MainActivity::class.java).also {
                                it.flags =
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                context.startActivity(it)
                            }
                        } else {
                            Toast.makeText(context, "First Verify Your Email", Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "login: Login Failed :- ${task.exception}")
                    }
                }
        } else {
            Toast.makeText(context, "Fill The Fields", Toast.LENGTH_SHORT).show()
        }

    }

    fun forgotPassword(email: String) {
        if (email.isNotEmpty()) {
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(context, "Password Reset", Toast.LENGTH_SHORT).show()
                }
                else{
                    Log.d(TAG, "forgotPassword: Failed")
                }
            }
        }
    }




}