package com.example.ownerapp.Fragments.productFrags

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ownerapp.R
import com.example.ownerapp.databinding.FragmentHomeBinding
import com.example.ownerapp.databinding.FragmentProductsBinding


class Products : Fragment() {

    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProductsBinding.inflate(inflater, container, false)

        return binding.root
    }


}