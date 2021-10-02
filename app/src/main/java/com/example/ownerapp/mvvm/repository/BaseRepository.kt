package com.example.ownerapp.mvvm.repository

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import com.example.ownerapp.Utils.Constants
import com.example.ownerapp.Utils.Constants.PLANS
import com.example.ownerapp.Utils.Constants.STORAGECATEGORIES
import com.example.ownerapp.activities.LoginActivity
import com.example.ownerapp.activities.MainActivity
import com.example.ownerapp.activities.ViewPlan
import com.example.ownerapp.data.Branch
import com.example.ownerapp.data.Plan
import com.example.ownerapp.data.Product
import com.example.ownerapp.data.ProductCategory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


abstract class BaseRepository(private var contextBase: Context) {

    private var mAuthBase = FirebaseAuth.getInstance()
    val categoryList = MutableLiveData<ArrayList<String>>()
    private val fDatabase = FirebaseDatabase.getInstance()
    private val branchesInfoRef = fDatabase.getReference(Constants.BRANCH_INFO)
    private val plansRef = fDatabase.getReference(PLANS)
    var storage = FirebaseStorage.getInstance()
    var storageRefProduct: StorageReference = storage.reference
    var storageRefCategory: StorageReference = storage.reference
    private val categoryInfo = fDatabase.getReference(Constants.CATEGORYINFO)
    private val categoryNames = fDatabase.getReference(Constants.CATEGORYNAMES)

    private val productsInfo = fDatabase.getReference(Constants.PRODUCTS)

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
        val branches: MutableLiveData<ArrayList<Branch>> = MutableLiveData<ArrayList<Branch>>()
        val tempList = ArrayList<Branch>(10)
        tempList.clear()
        branchesInfoRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                branches.value?.clear()
                dataSnapshot.children.forEach {
                    tempList.add(it.getValue(Branch::class.java)!!)
                    Log.d("TAG", "onDataChange: $tempList")
                }
                branches.value = tempList
                Log.d("TAG", "onDataChange:${branches.value} ")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("HENLO", "onCancelled: $error")
            }
        })
        return branches
    }

    fun sendUserToViewPlanActivity() {
        Intent(contextBase, ViewPlan::class.java).also {
            contextBase.startActivity(it)
        }
    }



    fun fetchAllCategoriesNames(): MutableLiveData<ArrayList<String>> {
        val list = ArrayList<String>()
        categoryNames.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot: DataSnapshot in snapshot.children) {
                    list.add(dataSnapshot.value.toString())
                }
                categoryList.value = list
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "onCancelled: $error")
            }

        })
        return categoryList
    }

    fun fetchAllPlans(): MutableLiveData<ArrayList<Plan>> {
        val plans: MutableLiveData<ArrayList<Plan>> = MutableLiveData<ArrayList<Plan>>()
        val tempList = ArrayList<Plan>(10)
        tempList.clear()
        plansRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                plans.value?.clear()
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
        val key = productsInfo.push().key.toString()

        product.key = key
        Log.d(TAG, "addProduct: Product $product\n\n")
        productsInfo.child(product.category.trim()).child(key).setValue(product)
            .addOnSuccessListener {
                Toast.makeText(contextBase, "Product Added Successfully", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(contextBase, "Try Again later", Toast.LENGTH_SHORT).show()
            }
        for (i in 0 until product.productImages.size) {
            val ref =
                storageRefProduct.child(Constants.PRODUCTS).child(product.category.trim())
                    .child(key).child(i.toString())
            ref.putFile(product.productImages[i].toUri()).addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    product.productImages[i] = it.toString()
                    Log.d(TAG, "addProduct: Download URL=${product.productImages[i]}")
                    productsInfo.child(product.category.trim()).child(key).child("productImages")
                        .child(i.toString()).setValue(product.productImages[i])
                }.addOnFailureListener {
                    Log.d(TAG, "addProduct: Errors ${it.message} \n\n ${it.cause}\n\n")
                }
            }.addOnFailureListener {
                Log.d(TAG, "addProduct: Failed at $i")
            }
        }




        Log.d(TAG, "addProduct: \n\n Product Images ${product.productImages.size}")


    }


    fun addCategory(category: ProductCategory) {
        val key = categoryInfo.push().key.toString()
        categoryInfo.child(key).setValue(category)
        categoryNames.child(key).setValue(category.name)

        val ref=storageRefCategory.child(STORAGECATEGORIES).child(key)
            ref.putFile(category.image.toUri())
            .addOnCompleteListener {
                if (it.isSuccessful)
                    ref.downloadUrl.addOnSuccessListener { it2 ->
                        Toast.makeText(contextBase, "Done", Toast.LENGTH_SHORT).show()
                        categoryInfo.child(key).child("image").setValue(it2.toString())
                    }
                else {
                    Log.d(TAG, "addCategory: Failed to add ${it.exception}")
                    Toast.makeText(contextBase, "Failed To Upload", Toast.LENGTH_SHORT).show()
                }
            }
    }

}