package com.example.ownerapp.Interfaces

import com.example.ownerapp.data.Cart

interface OrdersCallback {
    fun getOrdersCallback(list: ArrayList<Cart>)
}