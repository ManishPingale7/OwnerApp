package com.example.ownerapp.mvvm.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.ownerapp.Utils.Constants.PENDING
import com.example.ownerapp.data.Cart
import com.google.firebase.auth.FirebaseAuth

class MainRepository(private val contextMain: Context) : BaseRepository(contextMain) {

    fun getPendingOrders(): MutableLiveData<ArrayList<Cart>> {
        val tempList = ArrayList<Cart>()
        val mList = MutableLiveData<ArrayList<Cart>>()
        firestore.collection("Orders").whereEqualTo("Status", PENDING)
            .addSnapshotListener { value, error ->
                tempList.clear()
                error?.let {
                    Log.d("TAG", "getAllOrder: ERROR")
                    return@addSnapshotListener
                }
                value?.let { result ->
                    Log.d("TAG", "getAllOrder: RESULTING LIST: $result")
                    tempList.clear()
                    val docs = result.documents
                    docs.forEach {
                        val cart = it.toObject(Cart::class.java)
                        if (cart != null)
                            tempList.add(cart.copy(id = it.id))
                        Log.d("TAG", "getAllOrder: ORDERS: ${cart!!.copy(id = it.id)}")
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