package com.example.ownerapp.activities

import android.content.res.Configuration
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.example.ownerapp.R
import com.example.ownerapp.Utils.Constants.email
import com.example.ownerapp.databinding.ActivityLoginBinding
import com.example.ownerapp.di.components.DaggerFactoryComponent
import com.example.ownerapp.di.modules.FactoryModule
import com.example.ownerapp.di.modules.RepositoryModule
import com.example.ownerapp.mvvm.repository.AuthRepository
import com.example.ownerapp.mvvm.viewmodles.AuthViewModel
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var viewModel: AuthViewModel
    private lateinit var component: DaggerFactoryComponent

    var loggedIn = false
    lateinit var binding: ActivityLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()



        binding.btnLoginLg.setOnClickListener {
            val em = binding.logEmailEdit.text.toString()
            val pass = binding.logPassEdit.text.toString()
            if (em == email)
                viewModel.login(em, pass)
            else
                Toast.makeText(this, "Access Denied", Toast.LENGTH_SHORT).show()

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
            .factoryModule(FactoryModule(AuthRepository(this)))
            .build() as DaggerFactoryComponent
        viewModel = ViewModelProviders.of(this, component.getFactory())
            .get(AuthViewModel::class.java)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        resources.configuration.uiMode = Configuration.UI_MODE_NIGHT_YES
        setTheme(R.style.Theme_AppTheme_Dark)
    }

}