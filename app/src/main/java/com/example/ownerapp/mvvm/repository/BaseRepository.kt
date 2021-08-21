package com.example.ownerapp.mvvm.repository

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.ownerapp.Utils.Constants
import com.example.ownerapp.Utils.Constants.BRANCH_ID
import com.example.ownerapp.Utils.Constants.BRANCH_KEY
import com.example.ownerapp.Utils.Constants.BRANCH_NAME
import com.example.ownerapp.Utils.Constants.BRANCH_PASS
import com.example.ownerapp.activities.LoginActivity
import com.example.ownerapp.activities.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

abstract class BaseRepository(private var contextBase: Context) {


    private var mAuthBase = FirebaseAuth.getInstance()
    var curUser = mAuthBase.currentUser
    val fDatabase = FirebaseDatabase.getInstance()
    val branchesNameRef = fDatabase.getReference(Constants.BRANCHES_SPINNER)
    val branchesInfoRef = fDatabase.getReference(Constants.BRANCH_INFO)

    val branchesList = MutableLiveData<java.util.ArrayList<String>>()

    fun signOut() {
        mAuthBase.signOut()
    }

    fun sendUserToMainActivity() {
        Intent(contextBase, MainActivity::class.java).also {
            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            contextBase.startActivity(it)
        }
    }

    fun sendUserToLoginActivity() {
        Intent(contextBase, LoginActivity::class.java).also {
            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            contextBase.startActivity(it)
        }
    }

    fun fetchBranchNames(): MutableLiveData<ArrayList<String>> {
        val list = java.util.ArrayList<String>()
        branchesNameRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot: DataSnapshot in snapshot.children) {
                    list.add(dataSnapshot.value.toString())
                }
                branchesList.value = list
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(ContentValues.TAG, "onCancelled: $error")
            }
        })
        return branchesList
    }

    fun addNewBranch(name: String, branchID: String, branchPassword: String) {
        val key = branchesNameRef.push().key
        branchesNameRef.child(key.toString()).setValue(name).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(contextBase, "Branch Added", Toast.LENGTH_SHORT).show()
            }
        }
        val smallCaseName = name.lowercase()
        val result=smallCaseName.replace(" ", "");
        branchesInfoRef.child(result).child(BRANCH_NAME).setValue(name)
        branchesInfoRef.child(result).child(BRANCH_ID).setValue(branchID)
        branchesInfoRef.child(result).child(BRANCH_PASS).setValue(branchPassword)
    }


}