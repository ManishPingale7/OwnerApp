package com.example.ownerapp.Fragments.userTabFrags

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ownerapp.databinding.FragmentPlanExpiredBinding


class PlanExpiredFragment : Fragment() {
    private var _binding: FragmentPlanExpiredBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlanExpiredBinding.inflate(inflater, container, false)
        return binding.root
    }


}