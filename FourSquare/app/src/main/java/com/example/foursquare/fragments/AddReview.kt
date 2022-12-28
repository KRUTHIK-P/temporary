package com.example.foursquare.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.foursquare.R
import com.example.foursquare.databinding.FragmentAddReviewBinding
import com.example.foursquare.databinding.FragmentReviewBinding
import com.example.foursquare.viewmodels.LoginViewModel

class AddReview : Fragment() {

    private lateinit var addReviewBinding: FragmentAddReviewBinding
    private lateinit var viewModel: LoginViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        addReviewBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_review, container, false)



        return addReviewBinding.root
    }

}