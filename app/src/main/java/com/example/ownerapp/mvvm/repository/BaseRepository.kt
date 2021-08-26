package com.example.ownerapp.mvvm.repository

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ownerapp.Utils.Constants
import com.example.ownerapp.activities.LoginActivity
import com.example.ownerapp.data.Branch
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

    private lateinit var branchesList: LiveData<ArrayList<Branch>>

    fun signOut() {
        mAuthBase.signOut()
    }

    fun sendUserToMainActivity() {


    }


    fun sendUserToLoginActivity() {
        Intent(contextBase, LoginActivity::class.java).also {
            contextBase.startActivity(it)
        }
    }


    fun fetchBranches(): MutableLiveData<ArrayList<Branch>> {
        var branches: MutableLiveData<ArrayList<Branch>>? = MutableLiveData<ArrayList<Branch>>()
        var tempList = ArrayList<Branch>(10)
        tempList.clear()
        branchesInfoRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                branches?.value?.clear()
                dataSnapshot.children.forEach {
                    tempList.add(it.getValue(Branch::class.java)!!)
                    Log.d("TAG", "onDataChange: $tempList")
                }
                branches?.value = tempList
                Log.d("TAG", "onDataChange:${branches?.value} ")
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("HENLO", "onCancelled: $error")
            }
        })
        return branches!!
    }





}