package com.example.ownerapp.activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.example.ownerapp.R
import com.example.ownerapp.data.Branch
import com.example.ownerapp.databinding.ActivityAddNewBranchBinding
import com.example.ownerapp.di.components.DaggerFactoryComponent
import com.example.ownerapp.di.modules.FactoryModule
import com.example.ownerapp.di.modules.RepositoryModule
import com.example.ownerapp.mvvm.repository.NewBranchRepo
import com.example.ownerapp.mvvm.viewmodles.NewBranchViewModel
import com.google.firebase.auth.FirebaseAuth

class AddNewBranch : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var viewModel: NewBranchViewModel
    private lateinit var component: DaggerFactoryComponent
    lateinit var binding: ActivityAddNewBranchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewBranchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

        binding.continueBranch.setOnClickListener {
            val branchName = binding.newBranchName.text.toString()
            val branchID = binding.branchID.text.toString()
            val branchPassword = binding.branchPassword.text.toString()

            val upperString: String =
                branchName.substring(0, 1).uppercase() + branchName.substring(1).lowercase()

            Log.d(TAG, "onCreate: upperString $upperString")
            if (branchName.isNotEmpty() && branchID.isNotEmpty() && branchPassword.isNotEmpty()) {
                if (branchPassword.length >= 8) {
                    viewModel.repository.addNewBranch(Branch(upperString, branchID, branchPassword))
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(
                        this, "password should be at least 8 characters", Toast.LENGTH_SHORT
                    ).show()
                }

            } else
                Toast.makeText(this, "Fill the Fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun init() {
        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorprimarymain)
        mAuth = FirebaseAuth.getInstance()
        component = DaggerFactoryComponent.builder()
            .repositoryModule(RepositoryModule(this))
            .factoryModule(FactoryModule(NewBranchRepo(this)))
            .build() as DaggerFactoryComponent
        viewModel = ViewModelProviders.of(this, component.getFactory())
            .get(NewBranchViewModel::class.java)

    }
}