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
import com.example.foursquare.R
import com.example.foursquare.databinding.FragmentAboutUsBinding
import com.example.foursquare.databinding.FragmentFeedbackBinding
import com.example.foursquare.dataclass.AddFavouriteResponse
import com.example.foursquare.dataclass.GetAboutUsResponse
import com.example.foursquare.dataclass.GetFeedbackRequest
import com.example.foursquare.interfaces.Communicator
import com.example.foursquare.viewmodels.LoginViewModel

class AboutUs : Fragment() {


    private lateinit var aboutUsBinding: FragmentAboutUsBinding
    private lateinit var viewModel: LoginViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        aboutUsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_about_us, container, false)

        aboutUsBinding.backIv.setOnClickListener {
            val communicator = activity as Communicator
            communicator.gotoHome()
        }

        initViewModel()
        getAboutUs()
        return aboutUsBinding.root
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        viewModel.getAboutUsObserver()
            .observe(viewLifecycleOwner, Observer<GetAboutUsResponse?> {

                if (it == null) {
                    //Toast.makeText(this@Details.context, "No Places", Toast.LENGTH_SHORT).show()
                } else {
                    Log.d("response", it.toString())
                    aboutUsBinding.aboutUsTv.text = it[0].aboutUs
                }
            })
    }

    private fun getAboutUs() {
        viewModel.getAboutUs()
    }
}