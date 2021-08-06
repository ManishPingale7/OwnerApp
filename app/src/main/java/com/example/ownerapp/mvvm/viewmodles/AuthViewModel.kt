package com.example.ownerapp.mvvm.viewmodles

import androidx.lifecycle.ViewModel
import com.example.ownerapp.mvvm.repository.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class AuthViewModel(
    val repository: AuthRepository
) : ViewModel() {

    fun login(email: String, password: String) = CoroutineScope(IO).launch {
        repository.login(email, password)
    }




}