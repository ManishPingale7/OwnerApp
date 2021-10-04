package com.example.ownerapp.Fragments.mainFrags

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings.ACTION_SECURITY_SETTINGS
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
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
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.system.exitProcess


class Settings : Fragment() {

    private lateinit var viewModel: MainViewModel
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    var currentUser: FirebaseUser? = null
    private var mAuth = FirebaseAuth.getInstance()
    private val executor: ExecutorService = Executors.newSingleThreadExecutor()
    private var promptInfo: BiometricPrompt.PromptInfo? = null
    private lateinit var fingerprintManager: FingerprintManager


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
        initiateAuth()

        binding.viewAllBranches.setOnClickListener {
            startActivity(Intent(context, ViewAllBranches::class.java))
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

    private fun initiateAuth() {
        val keyguardManager = context?.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            fingerprintManager =
                context?.getSystemService(Context.FINGERPRINT_SERVICE) as FingerprintManager
            if (fingerprintManager.hasEnrolledFingerprints()) {
                //Fingerprint is enabled
                promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
                    .setTitle("This is the Title")
                    .setSubtitle("This is the subtitle")
                    .setDescription("This is the Description")
                    .setNegativeButtonText("Cancel")
                    .build()
            } else if (keyguardManager.isDeviceSecure) {
                //Fingerprint not enabled
                Toast.makeText(context, "Device is Secure", Toast.LENGTH_SHORT).show()
                promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setAllowedAuthenticators(BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                    .setTitle("This is the Title")
                    .setSubtitle("This is the subtitle")
                    .setDescription("This is the Description")
                    .build()
            } else if (!keyguardManager.isDeviceSecure) {
                Toast.makeText(
                    context,
                    "Add lock to your phone in order to use this app",
                    Toast.LENGTH_SHORT
                ).show()
                startActivity(Intent(ACTION_SECURITY_SETTINGS))
            }
        } else {
            TODO("Add if !API==M")
        }

        val biometricPrompt: BiometricPrompt =
            BiometricPrompt(
                requireActivity(),
                executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        activity?.let {
                            Log.d("rtgfdhj", "onAuthenticationError: $errString")
                            activity!!.runOnUiThread {
                                Toast.makeText(
                                    activity,
                                    errString,
                                    Toast.LENGTH_SHORT
                                ).show()

                                object : CountDownTimer(1500, 1000) {
                                    override fun onTick(millisUntilFinished: Long) {
                                        Log.d("rtgfdhj", "onTick: ")
                                    }

                                    override fun onFinish() {
                                        Log.d("rtgfdhj", "onFinish:hues ")
                                        exitProcess(0)
                                    }
                                }.start()
                            }
                        }
                    }

                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        activity?.let {
                            activity!!.runOnUiThread {
                                Toast.makeText(
                                    activity,
                                    "Authentication Successful",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        activity?.let {
                            activity!!.runOnUiThread {
                                Toast.makeText(
                                    activity,
                                    "Authentication Failed",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                })
        promptInfo?.let {
            biometricPrompt.authenticate(promptInfo!!)
        }
    }
}