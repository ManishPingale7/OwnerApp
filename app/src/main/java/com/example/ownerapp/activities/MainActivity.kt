package com.example.ownerapp.activities

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.ownerapp.Messaging.FirebaseService
import com.example.ownerapp.R
import com.example.ownerapp.databinding.ActivityMainBinding
import com.example.ownerapp.di.components.DaggerFactoryComponent
import com.example.ownerapp.di.modules.FactoryModule
import com.example.ownerapp.di.modules.RepositoryModule
import com.example.ownerapp.mvvm.repository.MainRepository
import com.example.ownerapp.mvvm.viewmodles.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging


class MainActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var viewModel: MainViewModel
    private lateinit var component: DaggerFactoryComponent
    private lateinit var database: FirebaseDatabase
    private lateinit var myRef: DatabaseReference
    private var currentuser: FirebaseUser? = null
    private val TAG = "mActivity"
    private lateinit var sharedPreferences: SharedPreferences
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

        setSupportActionBar(binding.toolbarMain)
//        val actionBar: ActionBar? = supportActionBar
//        actionBar!!.setDisplayHomeAsUpEnabled(true)
//        actionBar.setHomeButtonEnabled(true)

        val navController = findNavController(R.id.ContainerViewMain)
        val appBarConfig =
            AppBarConfiguration(setOf(R.id.branches, R.id.products, R.id.orders2, R.id.settings))
        setupActionBarWithNavController(navController, appBarConfig)
        binding.bottomNavigation.setupWithNavController(navController)


    }


    private fun init() {

        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE)
        //Toolbar stuff
        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorprimarymain)


        component = DaggerFactoryComponent.builder()
            .repositoryModule(RepositoryModule(this))
            .factoryModule(FactoryModule(MainRepository(this)))
            .build() as DaggerFactoryComponent
        viewModel = ViewModelProviders.of(this, component.getFactory())
            .get(MainViewModel::class.java)
        mAuth = FirebaseAuth.getInstance()


        currentuser = mAuth.currentUser
        checkUser()
        database = FirebaseDatabase.getInstance()
        myRef = database.reference
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//        resources.configuration.uiMode = Configuration.UI_MODE_NIGHT_YES
//        setTheme(R.style.Theme_AppTheme_Dark)

    }

    private fun checkUser() {
        mAuth = FirebaseAuth.getInstance()
        currentuser = mAuth.currentUser
        if (currentuser == null) {
            Log.d(TAG, "checkUser: User null")
            viewModel.sendUserToLoginActivity()
            finish()
        } else
            if (!sharedPreferences.getBoolean("isTokenAdded", false))
                setOwnerToken()

    }

    private fun setOwnerToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (!it.isSuccessful) {
                Log.d("TAG", "init: ERROR! ${it.exception}")
                return@addOnCompleteListener
            } else if (it.isSuccessful) {
                FirebaseService.sharedPrefs = sharedPreferences
                FirebaseService.token = it.result
                sharedPreferences.edit().putBoolean("isTokenAdded", true).apply()
                viewModel.setOwnerToken(FirebaseService.token)
            }
        }
    }
}