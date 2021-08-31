package com.example.ownerapp.mvvm.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ownerapp.mvvm.repository.*
import com.example.ownerapp.mvvm.viewmodles.AuthViewModel
import com.example.ownerapp.mvvm.viewmodles.MainViewModel
import com.example.ownerapp.mvvm.viewmodles.NewBranchViewModel
import com.example.ownerapp.mvvm.viewmodles.NewPlanViewModel

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
            modelClass.isAssignableFrom(NewPlanViewModel::class.java) -> {
                return NewPlanViewModel(repository = repository as PlanRepository) as T
            }

            modelClass.isAssignableFrom(NewBranchViewModel::class.java) -> {
                return NewBranchViewModel(repository = repository as NewBranchRepo) as T
            }
            else -> {
                throw IllegalArgumentException("ViewModel Not Found")
            }
        }
    }

}