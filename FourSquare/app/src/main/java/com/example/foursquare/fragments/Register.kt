package com.example.foursquare.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.foursquare.R
import com.example.foursquare.databinding.FragmentLoginBinding
import com.example.foursquare.databinding.FragmentRegisterBinding
import com.example.foursquare.dataclass.LoginResponse
import com.example.foursquare.dataclass.TokenManager
import com.example.foursquare.dataclass.User
import com.example.foursquare.interfaces.Communicator
import com.example.foursquare.storage.SharedPreferencesManager
import com.example.foursquare.viewmodels.LoginViewModel

class Register : Fragment(){
    private lateinit var registerBinding: FragmentRegisterBinding
    private lateinit var viewModel: LoginViewModel

    private lateinit var communicator: Communicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        registerBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)

        return registerBinding.root
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        viewModel.getLoginUserObserver().observe(viewLifecycleOwner, Observer<LoginResponse?> {

            if (it == null) {
                Toast.makeText(this@Register.context, "Failed to register", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@Register.context, it.message, Toast.LENGTH_SHORT)
                    .show()

                SharedPreferencesManager.getInstance(registerBinding.root.context).saveToken(
                    TokenManager(it.access_token, true)
                )
                communicator.gotoHome()
            }
        })
    }

    private fun register(user: User) {
        viewModel.loginUser(user)
    }
}