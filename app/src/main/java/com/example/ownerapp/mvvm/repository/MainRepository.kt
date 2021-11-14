package com.example.ownerapp.mvvm.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.ownerapp.data.Cart
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.toObjects

class MainRepository(private val contextMain: Context) : BaseRepository(contextMain) {

    fun getAllOrder(): MutableLiveData<ArrayList<Cart>> {
        val tempList = ArrayList<Cart>()
        val mList = MutableLiveData<ArrayList<Cart>>()
        firestore.collection("Orders")
            .addSnapshotListener { value, error ->
                tempList.clear()
                error?.let {
                    Log.d("TAG", "getAllOrder: ERROR")
                    return@addSnapshotListener
                }
                value?.let { result ->
                    Log.d("TAG", "getAllOrder: RESULTING LIST: $result")
                    tempList.clear()
                    result.toObjects<Cart>().forEach {
                        tempList.add(it)
                        Log.d("TAG", "getAllOrder: ORDERS: $it")
                    }
                    mList.value = tempList
                }
            }
        return mList
    }

    fun pushOwnerFcmToken(token: String?) =
        fDatabase.reference.child("OwnerFcmToken").setValue(token)


    private var mAuthMain = FirebaseAuth.getInstance()
    var curUserMain = mAuthMain.currentUser

}