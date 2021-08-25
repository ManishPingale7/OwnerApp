package com.example.ownerapp.mvvm.viewmodles

import androidx.lifecycle.ViewModel
import com.example.ownerapp.mvvm.repository.MainRepository
import com.example.ownerapp.mvvm.repository.PlanRepository

class NewPlanViewModel constructor(var repository: PlanRepository) : ViewModel() {

}