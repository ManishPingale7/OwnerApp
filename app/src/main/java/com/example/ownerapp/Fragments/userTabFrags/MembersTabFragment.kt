package com.example.ownerapp.Fragments.userTabFrags

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ownerapp.R
import com.example.ownerapp.databinding.FragmentHomeBinding
import com.example.ownerapp.databinding.FragmentMembersTabBinding


class MembersTabFragment : Fragment() {
    private var _binding: FragmentMembersTabBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMembersTabBinding.inflate(inflater, container, false)
        return binding.root
    }


}