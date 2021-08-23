package com.example.ownerapp.mvvm.viewmodles

import androidx.lifecycle.ViewModel
import com.example.ownerapp.data.Branch
import com.example.ownerapp.mvvm.repository.MainRepository

class MainViewModel constructor(var repository: MainRepository) : ViewModel() {
     val branches: ArrayList<Branch>

    init {
        branches = repository.fetchBranches()
    }

    fun signOut() {
        repository.signOut()
    }

    fun sendUserToLoginActivity() {
        repository.sendUserToLoginActivity()
    }

    fun addNewBranch(branch: Branch) {
        repository.addNewBranch(branch)
    }

    fun fetchBranches() = repository.fetchBranches()


    fun sendUserToMainActivity() {
        repository.sendUserToMainActivity()

    }
}