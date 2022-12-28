package com.example.foursquare.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foursquare.R
import com.example.foursquare.adapter.NearYouRecyclerViewAdapter
import com.example.foursquare.databinding.FragmentLunchBinding
import com.example.foursquare.dataclass.GetFavouritesRequest
import com.example.foursquare.dataclass.GetFavouritesResponse
import com.example.foursquare.dataclass.LatLangg
import com.example.foursquare.dataclass.NearYouResponse
import com.example.foursquare.storage.SharedPreferencesManager
import com.example.foursquare.viewmodels.LoginViewModel

class Lunch() : Fragment() {

    private lateinit var lunchBinding: FragmentLunchBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var latLangg: LatLangg
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        lunchBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_lunch, container, false)

        val latitude = arguments?.getDouble("latitude")
        val longitude = arguments?.getDouble("longitude")

        latitude?.let { lat ->
            longitude?.let { lon ->
                latLangg = LatLangg(lat, lon)
            }
        }

        initViewModel()
        getLunchPlaces(latLangg)

        return lunchBinding.root
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        viewModel.getLunchObserver().observe(viewLifecycleOwner) {

            if (it == null) {
                Toast.makeText(this@Lunch.context, "No Places", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("response", it.toString())

                val getFavouritesRequest = GetFavouritesRequest(latLangg.latitude, latLangg.longitude, "")
                val token = SharedPreferencesManager.getInstance(lunchBinding.root.context).tokenManager.token
                initViewModel1(it)
                if (token != null) {
                    getFavourites(token, getFavouritesRequest)
                }

            }
        }
    }

    private fun getLunchPlaces(latLangg: LatLangg) {
        viewModel.lunch(latLangg)
    }

    private fun initViewModel1(nearYouResponse: NearYouResponse) {
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        viewModel.getFavouritesObserver()
            .observe(viewLifecycleOwner, Observer<GetFavouritesResponse?> {

                if (it == null) {
                    //Toast.makeText(this@Details.context, "No Places", Toast.LENGTH_SHORT).show()
                } else {
                    Log.d("response", it.toString())

                    lunchBinding.recyclerview.layoutManager =
                        LinearLayoutManager(lunchBinding.root.context)
                    lunchBinding.recyclerview.adapter = NearYouRecyclerViewAdapter(
                        activity,
                        nearYouResponse,
                        lifecycleScope,
                        viewLifecycleOwner,
                        this,
                        it
                    )
                }
            })
    }

    private fun getFavourites(token: String, getFavouritesRequest: GetFavouritesRequest) {
        viewModel.getFavourites(token, getFavouritesRequest)
    }
}