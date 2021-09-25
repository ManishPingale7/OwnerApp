package com.example.ownerapp.mvvm.repository

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.ownerapp.Utils.Constants
import com.example.ownerapp.Utils.Constants.PLANS
import com.example.ownerapp.activities.LoginActivity
import com.example.ownerapp.activities.MainActivity
import com.example.ownerapp.activities.ViewPlan
import com.example.ownerapp.data.Branch
import com.example.ownerapp.data.Plan
import com.example.ownerapp.data.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

abstract class BaseRepository(private var contextBase: Context) {

    private var mAuthBase = FirebaseAuth.getInstance()
    var curUser = mAuthBase.currentUser
    private val fDatabase = FirebaseDatabase.getInstance()
    private val branchesInfoRef = fDatabase.getReference(Constants.BRANCH_INFO)
    private val plansRef = fDatabase.getReference(PLANS)

    fun signOut() {
        mAuthBase.signOut()
    }

    fun sendUserToMainActivity() {
        Intent(contextBase, MainActivity::class.java).also {
            contextBase.startActivity(it)
        }
    }


    fun sendUserToLoginActivity() {
        Intent(contextBase, LoginActivity::class.java).also {
            contextBase.startActivity(it)
        }
    }


    fun fetchBranches(): MutableLiveData<ArrayList<Branch>> {
        val branches: MutableLiveData<ArrayList<Branch>>? = MutableLiveData<ArrayList<Branch>>()
        val tempList = ArrayList<Branch>(10)
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
    fun sendUserToViewPlanActivity() {
        Intent(contextBase, ViewPlan::class.java).also {
            contextBase.startActivity(it)
        }
    }


    fun fetchAllPlans(): MutableLiveData<ArrayList<Plan>> {
        val plans: MutableLiveData<ArrayList<Plan>> = MutableLiveData<ArrayList<Plan>>()
        val tempList = ArrayList<Plan>(10)
        tempList.clear()
        plansRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                plans .value?.clear()
                dataSnapshot.children.forEach {
                    Log.d(TAG, "onDataChange: $it")
                    tempList.add(it.getValue(Plan::class.java)!!)
                    Log.d("TAG", "onDataChange: $tempList")
                }
                plans.value = tempList
                Log.d("TAG", "onDataChange:${plans.value} ")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("HENLO", "onCancelled: $error")
            }
        })
        return plans
    }

    fun addProduct(product: Product) {
//        private val fDatabase = FirebaseDatabase.getInstance()
//        private val branchesInfoRef = fDatabase.getReference(Constants.BRANCH_INFO)
        val productsInfo = fDatabase.getReference(Constants.PRODUCTS)
        val key = productsInfo.child(product.category).push().toString()
        productsInfo.child(product.category).child(key).setValue(product)
    }


}