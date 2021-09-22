package com.example.ownerapp.Fragments.mainFrags

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.ownerapp.activities.AddNewBranch
import com.example.ownerapp.activities.AddNewPlan
import com.example.ownerapp.activities.ProductsActivity
import com.example.ownerapp.activities.ViewAllBranches
import com.example.ownerapp.databinding.FragmentSettingsBinding
import com.example.ownerapp.di.components.DaggerFactoryComponent
import com.example.ownerapp.di.modules.FactoryModule
import com.example.ownerapp.di.modules.RepositoryModule
import com.example.ownerapp.mvvm.repository.MainRepository
import com.example.ownerapp.mvvm.viewmodles.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class Settings : Fragment() {

    private lateinit var viewModel: MainViewModel
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    var currentUser: FirebaseUser?=null
    var mAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        binding.addNewBranch.setOnClickListener {
            val intent = Intent(requireContext(), AddNewBranch::class.java)
            startActivity(intent)
        }
        init()

        binding.viewAllBranches.setOnClickListener {
            val intent = Intent(requireContext(), ViewAllBranches::class.java)
            startActivity(intent)
        }

        binding.viewAllProducts.setOnClickListener {
            val intent = Intent(requireContext(), ProductsActivity::class.java)
            startActivity(intent)
        }

        binding.addNewPlan.setOnClickListener {

            val intent = Intent(requireContext(), AddNewPlan::class.java)
            startActivity(intent)
        }
        binding.viewAllPlans.setOnClickListener {
            viewModel.sendUserToViewPlanActivity()
        }
        return binding.root
    }

    private fun init() {
        val component: DaggerFactoryComponent = DaggerFactoryComponent.builder()
            .repositoryModule(RepositoryModule(requireContext()))
            .factoryModule(FactoryModule(MainRepository(requireContext())))
            .build() as DaggerFactoryComponent
        viewModel =
            ViewModelProviders.of(this@Settings, component.getFactory())
                .get(MainViewModel::class.java)
        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth.currentUser

    }

}