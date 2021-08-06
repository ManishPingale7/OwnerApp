package com.example.ownerapp.mvvm.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ownerapp.mvvm.repository.AuthRepository
import com.example.ownerapp.mvvm.repository.BaseRepository
import com.example.ownerapp.mvvm.repository.MainRepository
import com.example.ownerapp.mvvm.viewmodles.AuthViewModel
import com.example.ownerapp.mvvm.viewmodles.MainViewModel

@Suppress("UNCHECKED_CAST")
class ModelFactory(
    private val repository: BaseRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(repository as AuthRepository) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository = repository as MainRepository) as T
            }
            else -> {
                throw IllegalArgumentException("ViewModel Not Found")
            }
        }
    }

}