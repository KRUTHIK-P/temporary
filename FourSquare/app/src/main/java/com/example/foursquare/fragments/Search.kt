package com.example.foursquare.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foursquare.R
import com.example.foursquare.adapter.SearchNearByYouRecyclerViewAdapter
import com.example.foursquare.adapter.SearchPlacesRecyclerViewAdapter
import com.example.foursquare.databinding.FragmentSearchBinding
import com.example.foursquare.dataclass.*
import com.example.foursquare.interfaces.Communicator
import com.example.foursquare.storage.SharedPreferencesManager
import com.example.foursquare.viewmodels.LoginViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class Search : Fragment() {

    private lateinit var searchBinding: FragmentSearchBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var latLangg: LatLangg

    private lateinit var mapFragment: SupportMapFragment
    private lateinit var googleMap: GoogleMap

    private lateinit var getSearchPlacesResponse: GetSearchPlacesResponse
    private var getFavouritesResponse: GetFavouritesResponse? = null
    private var searchQuery = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        searchBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)

        val latitude = arguments?.getDouble("latitude")
        val longitude = arguments?.getDouble("longitude")

        latitude?.let { lat ->
            longitude?.let { lon ->
                latLangg = LatLangg(lat, lon)
            }
        }

        val communicator = activity as Communicator
        searchBinding.apply {
            backIv.setOnClickListener {
                communicator.gotoHome()
            }

            toppickTv.setOnClickListener {
                communicator.gotoHomeParticularTab(1)
            }

            popularTv.setOnClickListener {
                communicator.gotoHomeParticularTab(2)
            }

            lunchTv.setOnClickListener {
                communicator.gotoHomeParticularTab(3)
            }

            coffeeTv.setOnClickListener {
                communicator.gotoHomeParticularTab(4)
            }

            filterIv.setOnClickListener {

            }

        }



        initViewModel1()
        getNearByPlaces(latLangg)

        searchBinding.searchSv.setOnQueryTextListener()

        searchBinding.mapViewTv.setOnClickListener {
            if (searchQuery.isNotEmpty()) {
//                val bundle = Bundle()
//                bundle.putDouble("latitude", latLangg.latitude)
//                bundle.putDouble("longitude", latLangg.longitude)
//                bundle.putString("searchQuery", searchQuery)
//                val mapView = MapView()
//                mapView.arguments = bundle
//                if (text == "Map View"){
//                    searchBinding.recyclerviewButton.visibility = View.VISIBLE
//                    searchBinding.recyclerview1.layoutManager =
//                        LinearLayoutManager(searchBinding.root.context, LinearLayoutManager.HORIZONTAL, false)
//                    searchBinding.recyclerview1.adapter = SearchPlacesRecyclerViewAdapter(activity, getSearchPlacesResponse, lifecycleScope)
//                }
//                activity?.supportFragmentManager?.beginTransaction()
//                    ?.replace(R.id.container, mapView)?.addToBackStack(null)?.commit()

                searchBinding.mapViewLyt.visibility = View.VISIBLE
                searchBinding.scrollView.visibility = View.GONE
                searchBinding.recyclerviewButton.visibility = View.GONE
                searchBinding.recyclervieww.layoutManager =
                    LinearLayoutManager(
                        searchBinding.root.context,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                searchBinding.recyclervieww.adapter = SearchPlacesRecyclerViewAdapter(
                    activity,
                    getSearchPlacesResponse,
                    lifecycleScope,
                    getFavouritesResponse,
                    viewLifecycleOwner,
                    this
                )

                mapFragment =
                    childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
                mapFragment.getMapAsync(OnMapReadyCallback { googleMap1 ->
                    googleMap = googleMap1

                    for (i in getSearchPlacesResponse) {
                        val coordinates = i.location.coordinates
                        val placeLatLng = LatLng(coordinates[1], coordinates[0])
                        googleMap?.apply {
                            addMarker(MarkerOptions().position(placeLatLng).title(i.placeName))
                            moveCamera(CameraUpdateFactory.newLatLngZoom(placeLatLng, 5f))
                        }
                    }
                })

            }
        }

        searchBinding.mapViewTv1.setOnClickListener {

            searchBinding.mapViewLyt.visibility = View.GONE
            searchBinding.recyclerviewButton.visibility = View.VISIBLE
            searchBinding.recyclervieww.layoutManager =
                LinearLayoutManager(
                    searchBinding.root.context
                )
            searchBinding.recyclervieww.adapter = SearchPlacesRecyclerViewAdapter(
                activity,
                getSearchPlacesResponse,
                lifecycleScope,
                getFavouritesResponse,
                viewLifecycleOwner,
                this
            )
        }

        return searchBinding.root
    }


    private fun SearchView.setOnQueryTextListener() {
        setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                searchBinding.scrollView.visibility = View.GONE

                searchQuery = newText

                if (searchQuery.isNotEmpty()) {
                    initViewModel2()
                    getSearchPlaces(
                        GetSearchPlacesRequest(
                            latLangg.latitude,
                            latLangg.longitude,
                            newText
                        )
                    )
                } else {
                    searchBinding.scrollView.visibility = View.VISIBLE
                    searchBinding.recyclerviewButton.visibility = View.GONE
                }

                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

        })
    }


    private fun initViewModel1() {
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        viewModel.getNearByPlacesObserver()
            .observe(viewLifecycleOwner, Observer<GetNearByPlacesResponse?> {

                if (it == null) {
                    //Toast.makeText(this@Search.context, "No Places", Toast.LENGTH_SHORT).show()
                } else {
                    Log.d("response", it.toString())

                    searchBinding.recyclerview.layoutManager =
                        LinearLayoutManager(searchBinding.root.context)
                    searchBinding.recyclerview.adapter =
                        SearchNearByYouRecyclerViewAdapter(it, searchBinding.searchSv)
                }
            })
    }

    private fun getNearByPlaces(latLangg: LatLangg) {
        viewModel.getNearByPlaces(latLangg)
    }

    private fun initViewModel2() {
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        viewModel.getSearchPlacesObserver()
            .observe(viewLifecycleOwner, Observer<GetSearchPlacesResponse?> {

                if (it == null) {
                    //Toast.makeText(this@Search.context, "No Places", Toast.LENGTH_SHORT).show()
                } else {
                    Log.d("response", it.toString())

                    val getFavouritesRequest = GetFavouritesRequest(latLangg.latitude, latLangg.longitude, "")
                    val sharedPreferencesManager = SharedPreferencesManager.getInstance(searchBinding.root.context)
                    val token = sharedPreferencesManager.tokenManager.token
                    if (sharedPreferencesManager.isLoggedIn) {
                        if (token != null) {
                            initViewModel3(it)
                            getFavourites(token, getFavouritesRequest)
                        }
                    }
                    else{
                        searchBinding.recyclerviewButton.visibility = View.VISIBLE
                        searchBinding.recyclerview1.layoutManager =
                            LinearLayoutManager(searchBinding.root.context)
                        searchBinding.recyclerview1.adapter = SearchPlacesRecyclerViewAdapter(
                            activity,
                            it,
                            lifecycleScope,
                            null,
                            viewLifecycleOwner,
                            this
                        )

                        getFavouritesResponse = null
                    }

                    this.getSearchPlacesResponse = it
                }
            })
    }

    private fun getSearchPlaces(data: GetSearchPlacesRequest) {
        viewModel.getSearchPlaces(data)
    }

    private fun initViewModel3(getSearchPlacesResponse: GetSearchPlacesResponse) {
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        viewModel.getFavouritesObserver()
            .observe(viewLifecycleOwner, Observer<GetFavouritesResponse?> {

                if (it == null) {
                    //Toast.makeText(this@Details.context, "No Places", Toast.LENGTH_SHORT).show()
                } else {
                    Log.d("response", it.toString())

                    getFavouritesResponse = it
                    searchBinding.recyclerviewButton.visibility = View.VISIBLE
                    searchBinding.recyclerview1.layoutManager =
                        LinearLayoutManager(searchBinding.root.context)
                    searchBinding.recyclerview1.adapter = SearchPlacesRecyclerViewAdapter(
                        activity,
                        getSearchPlacesResponse,
                        lifecycleScope,
                        it,
                        viewLifecycleOwner,
                        this
                    )
                }
            })
    }

    private fun getFavourites(token: String, getFavouritesRequest: GetFavouritesRequest) {
        viewModel.getFavourites(token, getFavouritesRequest)
    }

}



