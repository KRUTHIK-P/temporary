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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foursquare.R
import com.example.foursquare.adapter.ReviewRecyclerViewAdapter
import com.example.foursquare.databinding.FragmentReviewBinding
import com.example.foursquare.dataclass.GetAllReviewResponse
import com.example.foursquare.dataclass.Id
import com.example.foursquare.storage.SharedPreferencesManager
import com.example.foursquare.viewmodels.LoginViewModel

class Review : Fragment() {

    private lateinit var reviewBinding: FragmentReviewBinding
    private lateinit var viewModel: LoginViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        reviewBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_review, container, false)

        val id = arguments?.getString("id").toString()

        initViewModel()
        getAllReview(id)

        reviewBinding.backIv.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        reviewBinding.nameTv.text = arguments?.getString("place_name").toString()


        reviewBinding.addReviewIv.setOnClickListener {
            val sharedPreferencesManager =
                SharedPreferencesManager.getInstance(reviewBinding.root.context)
            if (sharedPreferencesManager.isLoggedIn) {
                val bundle = Bundle()
                bundle.putString("id", id)
                val addReview = AddReview()
                addReview.arguments = bundle
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.container, addReview)
                    ?.addToBackStack(null)?.commit()
            } else {
                Toast.makeText(
                    reviewBinding.root.context,
                    "please login to give review",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        return reviewBinding.root
    }


    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        viewModel.getReviewObserver().observe(viewLifecycleOwner, Observer<GetAllReviewResponse?> {

            if (it == null) {
                //Toast.makeText(this@Review.context, "No Places", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("response", it.toString())

                reviewBinding.recyclerview.layoutManager =
                    LinearLayoutManager(reviewBinding.root.context)
                reviewBinding.recyclerview.adapter =
                    ReviewRecyclerViewAdapter(activity, it.reviewText)
            }
        })
    }

    private fun getAllReview(id: String) {
        viewModel.getAllReview(Id(id))
    }
}