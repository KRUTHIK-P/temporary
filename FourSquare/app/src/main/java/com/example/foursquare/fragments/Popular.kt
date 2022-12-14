package com.example.foursquare.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foursquare.R
import com.example.foursquare.adapter.NearYouRecyclerViewAdapter
import com.example.foursquare.databinding.FragmentPopularBinding
import com.example.foursquare.dataclass.GetFavouritesRequest
import com.example.foursquare.dataclass.GetFavouritesResponse
import com.example.foursquare.dataclass.LatLangg
import com.example.foursquare.dataclass.NearYouResponse
import com.example.foursquare.storage.SharedPreferencesManager
import com.example.foursquare.viewmodels.LoginViewModel

class Popular() : Fragment() {

    private lateinit var popularBinding: FragmentPopularBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var latLangg: LatLangg

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        popularBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_popular, container, false)

        val latitude = arguments?.getDouble("latitude")
        val longitude = arguments?.getDouble("longitude")

        latitude?.let { lat ->
            longitude?.let { lon ->
                latLangg = LatLangg(lat, lon)
            }
        }

        initViewModel()
        getPopularPlaces(latLangg)

        return popularBinding.root
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        viewModel.getPopularObserver().observe(viewLifecycleOwner) {

            if (it == null) {
                Toast.makeText(this@Popular.context, "No Places", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("response", it.toString())

                val getFavouritesRequest = GetFavouritesRequest(latLangg.latitude, latLangg.longitude, "")
                val sharedPreferencesManager = SharedPreferencesManager.getInstance(popularBinding.root.context)
                val token = sharedPreferencesManager.tokenManager.token
                if (sharedPreferencesManager.isLoggedIn) {
                    if (token != null) {
                        initViewModel1(it)
                        getFavourites(token, getFavouritesRequest)
                    }
                }
                else {
                    popularBinding.recyclerview.layoutManager =
                        LinearLayoutManager(popularBinding.root.context)
                    popularBinding.recyclerview.adapter =
                        NearYouRecyclerViewAdapter(
                            activity,
                            it,
                            lifecycleScope,
                            viewLifecycleOwner,
                            this,
                            null
                        )
                }

            }
        }
    }

    private fun getPopularPlaces(latLangg: LatLangg) {
        viewModel.popular(latLangg)
    }

    private fun initViewModel1(nearYouResponse: NearYouResponse) {
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        viewModel.getFavouritesObserver()
            .observe(viewLifecycleOwner, Observer<GetFavouritesResponse?> {

                if (it == null) {
                    //Toast.makeText(this@Details.context, "No Places", Toast.LENGTH_SHORT).show()
                } else {
                    Log.d("response", it.toString())

                    popularBinding.recyclerview.layoutManager =
                        LinearLayoutManager(popularBinding.root.context)
                    popularBinding.recyclerview.adapter = NearYouRecyclerViewAdapter(
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

    override fun onResume() {
        super.onResume()

        initViewModel()
        getPopularPlaces(latLangg)
    }
}