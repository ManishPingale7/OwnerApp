package com.example.ownerapp.Fragments.mainFrags


import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.ownerapp.R
import com.example.ownerapp.databinding.FragmentHomeBinding
import com.example.ownerapp.di.components.DaggerFactoryComponent
import com.example.ownerapp.di.modules.FactoryModule
import com.example.ownerapp.di.modules.RepositoryModule
import com.example.ownerapp.mvvm.repository.MainRepository
import com.example.ownerapp.mvvm.viewmodles.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.eazegraph.lib.models.PieModel
import org.eazegraph.lib.models.ValueLinePoint
import org.eazegraph.lib.models.ValueLineSeries
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.system.exitProcess


class Home : Fragment() {
    private lateinit var viewModel: MainViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var currentUser: FirebaseUser? = null
    var mAuth = FirebaseAuth.getInstance()
    private val executor: ExecutorService = Executors.newSingleThreadExecutor()
    private var promptInfo: BiometricPrompt.PromptInfo? = null
    private lateinit var fingerprintManager: FingerprintManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        init()
        initiateAuth()

        binding.piechart.addPieSlice(
            PieModel(
                "R", 40.toFloat(),
                Color.parseColor("#FFA726")
            )
        )
        binding.piechart.addPieSlice(
            PieModel(
                "Python", 20.toFloat(),
                Color.parseColor("#66BB6A")
            )
        )
        binding.piechart.addPieSlice(
            PieModel(
                "C++", 20.toFloat(),
                Color.parseColor("#EF5350")
            )
        )
        binding.piechart.addPieSlice(
            PieModel(
                "Java", 20.toFloat(),
                Color.parseColor("#29B6F6")
            )
        )

        val series = ValueLineSeries()
        series.color = resources.getColor(R.color.demo_green)
        series.addPoint(ValueLinePoint("Start", 0f))
        series.addPoint(ValueLinePoint("Jan", 3000f))
        series.addPoint(ValueLinePoint("Feb", 4000f))
        series.addPoint(ValueLinePoint("Mar", 3200f))
        series.addPoint(ValueLinePoint("Apr", 4000f))
        series.addPoint(ValueLinePoint("Mai", 5000f))
        series.addPoint(ValueLinePoint("Jun", 4232f))
        series.addPoint(ValueLinePoint("Jul", 4478f))
        series.addPoint(ValueLinePoint("Aug", 3786f))
        series.addPoint(ValueLinePoint("Sep", 5000f))
        series.addPoint(ValueLinePoint("Oct", 5000f))
        series.addPoint(ValueLinePoint("Nov", 4000f))
        series.addPoint(ValueLinePoint("Dec", 3000f))
        series.addPoint(ValueLinePoint("End", 2000f))
        binding.cubiclinechart.addSeries(series)
        binding.cubiclinechart.startAnimation()

        return binding.root
    }


    private fun init() {
        val component: DaggerFactoryComponent = DaggerFactoryComponent.builder()
            .repositoryModule(RepositoryModule(requireContext()))
            .factoryModule(FactoryModule(MainRepository(requireContext())))
            .build() as DaggerFactoryComponent
        viewModel =
            ViewModelProviders.of(this@Home, component.getFactory())
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
                startActivity(Intent(Settings.ACTION_SECURITY_SETTINGS))
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

