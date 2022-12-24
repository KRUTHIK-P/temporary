package com.example.foursquare.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foursquare.R
import com.example.foursquare.adapter.NearYouRecyclerViewAdapter
import com.example.foursquare.databinding.FragmentCoffeeBinding
import com.example.foursquare.dataclass.LatLangg
import com.example.foursquare.viewmodels.LoginViewModel

class Coffee() : Fragment() {

    private lateinit var coffeeBinding: FragmentCoffeeBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var latLangg: LatLangg

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        coffeeBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_coffee, container, false)

        val latitude = arguments?.getDouble("latitude")
        val longitude = arguments?.getDouble("longitude")

        latitude?.let { lat ->
            longitude?.let { lon ->
                latLangg = LatLangg(lat, lon)
            }
        }

        initViewModel()
        getCoffeePlaces(latLangg)

        return coffeeBinding.root
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        viewModel.getCoffeeObserver().observe(viewLifecycleOwner) {

            if (it == null) {
                Toast.makeText(this@Coffee.context, "No Places", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("response", it.toString())

                coffeeBinding.recyclerview.layoutManager =
                    LinearLayoutManager(coffeeBinding.root.context)
                coffeeBinding.recyclerview.adapter = NearYouRecyclerViewAdapter(
                    activity,
                    it,
                    lifecycleScope
                )
            }
        }
    }

    private fun getCoffeePlaces(latLangg: LatLangg) {
        viewModel.coffee(latLangg)
    }
}