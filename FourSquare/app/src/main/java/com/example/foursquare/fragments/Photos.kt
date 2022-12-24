package com.example.foursquare.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.foursquare.R
import com.example.foursquare.adapter.PhotosGVAdapter
import com.example.foursquare.databinding.FragmentPhotosBinding
import com.example.foursquare.dataclass.GetReviewImageResponse
import com.example.foursquare.dataclass.Id
import com.example.foursquare.viewmodels.LoginViewModel

class Photos : Fragment() {

    private lateinit var photosBinding: FragmentPhotosBinding
    private lateinit var viewModel: LoginViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        photosBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_photos, container, false)

        val id = arguments?.getString("id").toString()

        photosBinding.nameTv.text = arguments?.getString("place_name").toString()
        initViewModel()
        getReviewImage(id)

        photosBinding.backIv.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        return photosBinding.root
    }


    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        viewModel.getReviewImageObserver()
            .observe(viewLifecycleOwner, Observer<GetReviewImageResponse?> {

                if (it == null) {
                    //Toast.makeText(this@Details.context, "No Places", Toast.LENGTH_SHORT).show()
                } else {
                    Log.d("response", it.toString())

                    val reviewImageList = mutableListOf<String>()
                    for (i in it.reviewImage) {
                        reviewImageList.addAll(i.image)
                    }

                    val adapter = PhotosGVAdapter(
                        photosBinding.root.context,
                        it.reviewImage,
                        reviewImageList,
                        lifecycleScope,
                        activity,
                        photosBinding.nameTv.text.toString()
                    )
                    photosBinding.photosGv.adapter = adapter
                }
            })
    }

    private fun getReviewImage(id: String) {
        val idObject = Id(id)
        viewModel.getReviewImage(idObject)
    }
}