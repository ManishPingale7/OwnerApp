package com.example.ownerapp.mvvm.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.ownerapp.Utils.Constants
import com.example.ownerapp.Utils.Constants.BRANCHES
import com.example.ownerapp.data.Branch
import com.example.ownerapp.data.Plan
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class NewBranchRepo(private var contextBranch: Context): BaseRepository(contextBranch) {
    var mAuthBranch=FirebaseAuth.getInstance()
    private val fDatabase = FirebaseDatabase.getInstance()
    private val branchesInfoRef = fDatabase.getReference(Constants.BRANCH_INFO)
    private val branchesNameRef = fDatabase.getReference(Constants.BRANCHES_SPINNER)




    fun addNewBranch(branch: Branch) {
        val key = branchesNameRef.push().key
        branchesNameRef.child(key.toString()).setValue(branch.branchName).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(contextBranch, "Branch Added", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(contextBranch, "Cannot add branch \nTry later", Toast.LENGTH_SHORT)
                    .show()
                Log.d("HENLO", "addNewBranch: HEYY ${it.exception}")
            }
        }
        val result = branch.branchName.lowercase().replace(" ", "")
        branchesInfoRef.child(result).setValue(branch)
    }

}