package com.example.ownerapp.mvvm.viewmodles

import androidx.lifecycle.ViewModel
import com.example.ownerapp.data.Branch
import com.example.ownerapp.mvvm.repository.MainRepository

class MainViewModel constructor(var repository: MainRepository) : ViewModel() {


    fun fetchAllBranches() = repository.fetchBranches()

    fun signOut() {
        repository.signOut()
    }

    fun sendUserToLoginActivity() {
        repository.sendUserToLoginActivity()
    }

    fun getAllPlans() = repository.fetchAllPlans()
    fun sendUserToViewPlanActivity() =repository.sendUserToViewPlanActivity()
}