package com.example.ownerapp.mvvm.viewmodles

import androidx.lifecycle.ViewModel
import com.example.ownerapp.mvvm.repository.MainRepository

class MainViewModel constructor(var repository: MainRepository) : ViewModel() {

    fun signOut() {
        repository.signOut()
    }
}