package com.example.ownerapp.mvvm.repository

import android.content.Context
import android.widget.Toast
import com.example.ownerapp.Utils.Constants
import com.example.ownerapp.data.Plan
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class PlanRepository(private var contextPlan: Context): BaseRepository(contextPlan) {
    private val fDatabase = FirebaseDatabase.getInstance()
    private val planRef = fDatabase.getReference(Constants.PLAN)


    fun addNewPlan(plan: Plan) {
        val key=planRef.push().key.toString()
        plan.key= key
        planRef.child(key).setValue(plan).addOnSuccessListener {
            Toast.makeText(contextPlan, "Plan Added Successfully", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(contextPlan, "Try Again later", Toast.LENGTH_SHORT).show()
        }
    }

}