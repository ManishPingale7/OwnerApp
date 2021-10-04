package com.example.ownerapp.Fragments.mainFrags


import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        Log.d("hee", "onCreateView: In the Home")

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

}
