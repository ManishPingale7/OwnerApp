package com.example.ownerapp.Fragments.productFrags

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ownerapp.Adapters.CartAdapter
import com.example.ownerapp.Interfaces.OrdersCallback
import com.example.ownerapp.data.Cart
import com.example.ownerapp.databinding.FragmentOrdersBinding
import com.example.ownerapp.di.components.DaggerFactoryComponent
import com.example.ownerapp.di.modules.FactoryModule
import com.example.ownerapp.di.modules.RepositoryModule
import com.example.ownerapp.mvvm.repository.MainRepository
import com.example.ownerapp.mvvm.viewmodles.MainViewModel

class Orders : Fragment() {
    private lateinit var cartAdapter: CartAdapter
    private lateinit var component: DaggerFactoryComponent
    private lateinit var binding: FragmentOrdersBinding
    private lateinit var viewModel: MainViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrdersBinding.inflate(inflater, container, false)

        init()


        return binding.root
    }

    private fun init() {
        component = DaggerFactoryComponent.builder()
            .repositoryModule(RepositoryModule(requireContext()))
            .factoryModule(FactoryModule(MainRepository(requireContext())))
            .build() as DaggerFactoryComponent
        viewModel = ViewModelProviders.of(this, component.getFactory())
            .get(MainViewModel::class.java)
        binding.apply {
            recyclerViewCartItems.apply {
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
        }

        viewModel.getAllOrders(object : OrdersCallback {
            override fun getOrdersCallback(list: ArrayList<Cart>) {
                cartAdapter = CartAdapter(context!!, list)
                binding.apply {
                    recyclerViewCartItems.apply {
                        adapter = cartAdapter
                    }
                }
            }
        })

    }

}