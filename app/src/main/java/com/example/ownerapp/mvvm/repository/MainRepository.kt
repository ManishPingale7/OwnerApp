package com.example.ownerapp.mvvm.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.ownerapp.Adapters.Cart
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MainRepository(private val contextMain: Context) : BaseRepository(contextMain) {

    fun getAllOrder(): MutableLiveData<ArrayList<Cart>> {

        val ordersList = MutableLiveData<ArrayList<Cart>>()
        val tempList = ArrayList<Cart>()
        val ref = fDatabase.reference.child("Orders")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    tempList.add(it.getValue(Cart::class.java)!!)
                    Log.d("TAG", "onDataChange: templist ${tempList[0]}")
                }
                ordersList.value = tempList
            }

            override fun onCancelled(error: DatabaseError) {
                //TODO: WHAT TO DO IF CANCELLED
            }
        })
        Log.d("TAG", "getAllOrder: HERE IS LIST ${ordersList.value}")
        Log.d("TAG", "getAllOrder: HERE IS LIST12 $tempList")
        return ordersList
    }

    fun pushOwnerFcmToken(token: String?) =
        fDatabase.reference.child("OwnerFcmToken").setValue(token)


    private var mAuthMain = FirebaseAuth.getInstance()
    var curUserMain = mAuthMain.currentUser

}