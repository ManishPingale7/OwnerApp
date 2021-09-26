package com.example.ownerapp.Fragments.productFrags

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.example.ownerapp.R
import com.example.ownerapp.activities.AddNewCategory
import com.example.ownerapp.activities.AddNewProduct
import com.example.ownerapp.databinding.FragmentProductsBinding


class Products : Fragment() {

    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!

    private var clicked = false
    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.rotate_open_anim
        )
    }
    private val rotate_close: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.rotate_close_anim
        )
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.from_bottom_anim
        )
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.to_bottom_anim
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        binding.fabAddProduct.setOnClickListener {
            Intent(requireContext(), AddNewProduct::class.java).also {
                startActivity(it)
            }
        }

        binding.fabMain.setOnClickListener {
            addBtnClicked()

        }
        binding.fabAddCategory.setOnClickListener {
            Intent(requireContext(), AddNewCategory::class.java).also {
                startActivity(it)
            }

        }



        return binding.root
    }

    private fun addBtnClicked() {
        Log.d(TAG, "addBtnClicked: $clicked")
        setVisibility(clicked)
        setAnim(clicked)
        clicked = !clicked
    }

    private fun setAnim(clicked: Boolean) {
        if (!clicked) {
            binding.fabAddCategory.startAnimation(fromBottom)
            binding.fabAddProduct.startAnimation(fromBottom)
            binding.fabMain.startAnimation(rotateOpen)
        } else {
            binding.fabAddCategory.startAnimation(toBottom)
            binding.fabAddProduct.startAnimation(toBottom)
            binding.fabMain.startAnimation(rotate_close)
        }
    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            binding.fabAddCategory.visibility = View.VISIBLE
            binding.fabAddProduct.visibility = View.VISIBLE
        } else {
            binding.fabAddCategory.visibility = View.INVISIBLE
            binding.fabAddProduct.visibility = View.INVISIBLE
        }
    }


}