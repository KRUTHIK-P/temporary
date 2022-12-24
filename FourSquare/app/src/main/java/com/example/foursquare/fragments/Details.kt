package com.example.foursquare.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.allViews
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.foursquare.R
import com.example.foursquare.databinding.FragmentDetailsBinding
import com.example.foursquare.dataclass.GetParticularPlaceResponse
import com.example.foursquare.dataclass.Id
import com.example.foursquare.interfaces.Communicator
import com.example.foursquare.viewmodels.LoginViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream

class Details : Fragment() {

    private lateinit var detailsBinding: FragmentDetailsBinding
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var googleMap: GoogleMap

    private lateinit var viewModel: LoginViewModel
    private var overallRating = ""
    private var givenRating = 0


    @SuppressLint("RestrictedApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        detailsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)

        detailsBinding.backIv.setOnClickListener {
            val communicator = activity as Communicator
            communicator.gotoHome()
        }

        val id = arguments?.getString("id").toString()
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(OnMapReadyCallback { googleMap1 ->
            googleMap = googleMap1

            initViewModel()
            getParticularPlace(id)
        })

        detailsBinding.ratingLyt.setOnClickListener {
            val view = View.inflate(detailsBinding.root.context, R.layout.custom_dailogbox, null)
            val builder = AlertDialog.Builder(detailsBinding.root.context)
            builder.setView(view)

            val dialog = builder.create()
            dialog.window?.apply {
                setBackgroundDrawableResource(android.R.color.transparent)
            }

            view.apply {
                findViewById<ImageView>(R.id.close_iv).setOnClickListener {
                    dialog.dismiss()
                }

                findViewById<TextView>(R.id.overall_rating_tv).text = overallRating

                val allStars = arrayListOf<ImageView>(
                    findViewById(R.id.star1_iv),
                    findViewById(R.id.star2_iv),
                    findViewById(R.id.star3_iv),
                    findViewById(R.id.star4_iv),
                    findViewById(R.id.star5_iv)
                )

                for (i in 0 until allStars.size){
                    allStars[i].setOnClickListener {
                        for (k in 0 until allStars.size) {
                            allStars[k].setImageResource(R.drawable.unfilled_star)
                        }

                        for (j in 0..i){
                            allStars[j].setImageResource(R.drawable.filled_star)
                        }

                        givenRating = i
                    }
                }

                findViewById<TextView>(R.id.submit_tv).setOnClickListener {
                    initViewModel1()
                    addRating()
                }
            }
            dialog.show()

        }

        detailsBinding.photosLyt.setOnClickListener {
            val photos = Photos()
            val bundle = Bundle()
            bundle.putString("id", id)
            bundle.putString("place_name", detailsBinding.nameTv.text.toString())
            photos.arguments = bundle
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.container, photos)?.addToBackStack(null)?.commit()
        }

        detailsBinding.reviewLyt.setOnClickListener {
            val review = Review()
            val bundle = Bundle()
            bundle.putString("id", id)
            bundle.putString("place_name", detailsBinding.nameTv.text.toString())
            review.arguments = bundle
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.container, review)?.addToBackStack(null)?.commit()
        }

        return detailsBinding.root
    }

    private fun addRating() {
    }

    private fun initViewModel1() {
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        viewModel.getParticularPlaceObserver()
            .observe(viewLifecycleOwner, Observer<GetParticularPlaceResponse?> {

                if (it == null) {
                    //Toast.makeText(this@Details.context, "No Places", Toast.LENGTH_SHORT).show()
                } else {
                    Log.d("response", it.toString())

                    val distance = arguments?.getString("distance").toString()
                    detailsBinding.apply {
                        nameTv.text = it.placeName
                        var url = it.placeImage

                        if (!url.contains("https"))
                            url = "${url.substring(0..3)}s${url.substring(4)}"

                        loadImageFromWebUrl(url, placeImageLyt)
                        categoryTv.text = it.category
                        overviewTv.text = it.overview
                        addressTv.text = it.address
                        phoneTv.text = "+91 ${it.placePhone}"
                        driveTv.text = "Drive: ${distance}km"

                        it.location.coordinates.apply {
                            val placeLatLng = LatLng(get(1), get(0))
                            googleMap?.apply {
                                addMarker(MarkerOptions().position(placeLatLng).title(it.placeName))
                                moveCamera(CameraUpdateFactory.newLatLngZoom(placeLatLng, 18f))
                            }
                        }

                        detailsBinding.apply {
                            val unFilledStarIds = mutableListOf<ImageView>(
                                unfilledStar1, unfilledStar2, unfilledStar3, unfilledStar4, unfilledStar5
                            )
                            val filledStarLytIds = mutableListOf<ConstraintLayout>(
                                filledStar1Lyt, filledStar2Lyt, filledStar3Lyt, filledStar4Lyt, filledStar5Lyt
                            )
                            val filledStarIds = mutableListOf<ImageView>(
                                filledStar1, filledStar2, filledStar3, filledStar4, filledStar5
                            )
                            overallRating = it.rating.toString()
                            for (i in 0 until it.rating.toInt()){
                                unFilledStarIds[i].setImageResource(R.drawable.filled_star)
                            }

                            val pointValue = overallRating.substringAfter(".").toInt()
                            if(pointValue != 0) {
                                val  pointValuee = pointValue.toFloat()/10
                                val nextStarWidth = pointValuee * 20
//                                val params = ConstraintLayout.LayoutParams(
//                                    nextStarWidth.toInt()+17,
//                                    ConstraintLayout.LayoutParams.WRAP_CONTENT
//                                )
//                                filledStarLytIds[it.rating.toInt()].layoutParams = params
                                filledStarLytIds[it.rating.toInt()].layoutParams.width = nextStarWidth.toInt()+17
                                filledStarIds[it.rating.toInt()].visibility = View.VISIBLE
                            }
                        }
                    }
                }
            })
    }

    private fun getParticularPlace(id: String) {
        val idObject = Id(id)
        viewModel.getParticularPlace(idObject)
    }

    private fun loadImageFromWebUrl(url: String, placeImage: LinearLayout) {
        lifecycleScope.launch(Dispatchers.IO) {
            val image = try {
                val iStream = java.net.URL(url).content as InputStream
                Drawable.createFromStream(iStream, "placeImage")
            } catch (e: Exception) {
                null
            }

            image?.let {
                withContext(Dispatchers.Main) {
                    placeImage.background = it
                }
            }
        }
    }
}