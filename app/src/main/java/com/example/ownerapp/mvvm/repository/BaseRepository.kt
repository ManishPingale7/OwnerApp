package com.example.ownerapp.mvvm.repository

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.ownerapp.data.Branch
import com.example.ownerapp.Utils.Constants
import com.example.ownerapp.activities.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

abstract class BaseRepository(private var contextBase: Context) {


    private var mAuthBase = FirebaseAuth.getInstance()
    var curUser = mAuthBase.currentUser
    private val fDatabase = FirebaseDatabase.getInstance()
    private val branchesNameRef = fDatabase.getReference(Constants.BRANCHES_SPINNER)
    private val branchesInfoRef = fDatabase.getReference(Constants.BRANCH_INFO)

    val branchesList = MutableLiveData<java.util.ArrayList<String>>()

    fun signOut() {
        mAuthBase.signOut()
    }

    fun sendUserToMainActivity() {


    }


    fun sendUserToLoginActivity() {
        Intent(contextBase, LoginActivity::class.java ).also {
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

    fun addNewBranch(branch: Branch) {
        val key = branchesNameRef.push().key
        branchesNameRef.child(key.toString()).setValue(branch.branchName).addOnCompleteListener {
            if (it.isSuccessful)
                Toast.makeText(contextBase, "Branch Added", Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(contextBase, "Cannot add branch \nTry later", Toast.LENGTH_SHORT).show()
        }
        val result = branch.branchName.lowercase().replace(" ", "");
        branchesInfoRef.child(result).setValue(branch)
    }


}