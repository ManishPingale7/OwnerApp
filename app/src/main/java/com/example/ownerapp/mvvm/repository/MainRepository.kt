package com.example.ownerapp.mvvm.repository

import android.content.Context
import android.util.Log
import com.example.ownerapp.Interfaces.OrdersCallback
import com.example.ownerapp.data.Cart
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.toObjects

class MainRepository(private val contextMain: Context) : BaseRepository(contextMain) {

    fun getAllOrder(callback: OrdersCallback) {
        val tempList = ArrayList<Cart>()
        firestore.collection("Orders")
            .addSnapshotListener { value, error ->
                tempList.clear()
                error?.let {
                    Log.d("TAG", "getAllOrder: ERROR")
                    return@addSnapshotListener
                }
                value?.let { result ->
                    tempList.clear()
                    result.toObjects<Cart>().forEach {
                        tempList.add(it)
                    }
                    callback.getOrdersCallback(tempList)
                }
            }
    }

    fun pushOwnerFcmToken(token: String?) =
        fDatabase.reference.child("OwnerFcmToken").setValue(token)


    private var mAuthMain = FirebaseAuth.getInstance()
    var curUserMain = mAuthMain.currentUser

}