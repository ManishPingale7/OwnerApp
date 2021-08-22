package com.example.ownerapp.activities

import android.content.Intent
import android.os.Bundle
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
import com.example.ownerapp.mvvm.repository.MainRepository
import com.example.ownerapp.mvvm.viewmodles.MainViewModel
import com.google.firebase.auth.FirebaseAuth

class AddNewBranch : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var viewModel: MainViewModel
    private lateinit var component: DaggerFactoryComponent
    lateinit var binding: ActivityAddNewBranchBinding
    var branchList = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewBranchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

        binding.continueBranch.setOnClickListener {
            val branchName = binding.newBranchName.text.toString()
            val branchID = binding.branchID.text.toString()
            val branchPassword = binding.branchPassword.text.toString()

            if (branchName.isNotEmpty() && branchID.isNotEmpty() && branchPassword.isNotEmpty()) {
                viewModel.addNewBranch(Branch(branchName, branchID, branchPassword))
               val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            } else
                Toast.makeText(this, "Fill the Fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun init() {
        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.my_statusbar_color)
        mAuth = FirebaseAuth.getInstance()
        component = DaggerFactoryComponent.builder()
            .repositoryModule(RepositoryModule(this))
            .factoryModule(FactoryModule(MainRepository(this)))
            .build() as DaggerFactoryComponent
        viewModel = ViewModelProviders.of(this, component.getFactory())
            .get(MainViewModel::class.java)
    }
}