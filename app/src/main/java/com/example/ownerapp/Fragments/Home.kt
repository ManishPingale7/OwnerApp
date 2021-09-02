package com.example.ownerapp.Fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
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


class Home : Fragment() {
    private lateinit var viewModel: MainViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var currentUser: FirebaseUser? = null
    var mAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        init()
        binding.pieChartHome.addPieSlice(
            PieModel(
                "Members", 80F,
                Color.parseColor("#66BB6A")
            )
        )
        binding.pieChartHome.addPieSlice(
            PieModel(
                "InActive", 20F,
                Color.parseColor("#FFA726")
            )
        )


        val series = ValueLineSeries()
        series.color = -0xa9480f
        series.addPoint(ValueLinePoint("Start", 0f))
        series.addPoint(ValueLinePoint("Jan", 1000f))
        series.addPoint(ValueLinePoint("Feb", 3000f))
        series.addPoint(ValueLinePoint("Mar", 3200f))
        series.addPoint(ValueLinePoint("Apr", 6000f))
        series.addPoint(ValueLinePoint("Mai", 1000f))
        series.addPoint(ValueLinePoint("Jun", 3232f))
        series.addPoint(ValueLinePoint("Jul", 2478f))
        series.addPoint(ValueLinePoint("Aug", 12786f))
        series.addPoint(ValueLinePoint("Sep", 1000f))
        series.addPoint(ValueLinePoint("Oct", 3000f))
        series.addPoint(ValueLinePoint("Nov", 1000f))
        series.addPoint(ValueLinePoint("Dec", 3000f))
        series.addPoint(ValueLinePoint("End", 0f))
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
}

