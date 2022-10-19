package com.robosoft.passwordmanagermobile.fragments

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.robosoft.fragmentlifecycle.Communicator
import com.robosoft.passwordmanagermobile.R
import com.robosoft.passwordmanagermobile.databinding.FragmentSignInBinding
import com.robosoft.passwordmanagermobile.db.UserDB
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class SignIn : Fragment() {

    private lateinit var signInBinding: FragmentSignInBinding

    private lateinit var userDB: UserDB

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        signInBinding = FragmentSignInBinding.inflate(inflater, container, false)

        signInBinding.passwordToggleImageButton.setOnClickListener {

            if (signInBinding.mPinEt.inputType == InputType.TYPE_CLASS_NUMBER) {
                signInBinding.passwordToggleImageButton.setImageResource(R.drawable.ic_baseline_visibility_off_24)
                signInBinding.mPinEt.inputType =
                    InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
            } else {
                signInBinding.passwordToggleImageButton.setImageResource(R.drawable.ic_baseline_visibility_24)
                signInBinding.mPinEt.inputType = InputType.TYPE_CLASS_NUMBER
            }
        }

        signInBinding.signInBn.setOnClickListener {
            checkData()
        }

        return signInBinding.root
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun checkData() {

        var signInFlag = false

        var mobileNumberEditTextFlag = false
        val mobileNumberEditText = signInBinding.mobileNumberEt
        val mobileNumber = mobileNumberEditText.text.toString()

        if (!validateMobileNumber(mobileNumber)) {
            mobileNumberEditText.error = "Invalid Mobile Number"
        } else mobileNumberEditTextFlag = true


        var mPinEditTextFlag = false
        val mPinEditText = signInBinding.mPinEt
        val mPin = mPinEditText.text.toString()

        if (!validateMPin(mPin)) {
            mPinEditText.error = "MPin should be 4 digits"
        } else mPinEditTextFlag = true

        if (mobileNumberEditTextFlag and mPinEditTextFlag) {
            //val user = User(null, mobbileNumber, mPin)
            GlobalScope.launch(Dispatchers.IO) {

                userDB = UserDB.getDatabase(signInBinding.root.context)
                val usersList = userDB.userDao().getAll()
                val size = usersList.size

                for (index in 0 until size - 1) {
                    if (mobileNumber == usersList[index].phoneNumber) {
                        signInFlag = true
                        if (mPin == usersList[index].mPin){
                            val communicator = activity as Communicator
                            communicator.passSignInControl()
                        }
                        else Snackbar.make(signInBinding.root,"Wrong Password",Snackbar.LENGTH_SHORT).show()
                    }
                }

                if (!signInFlag)
                    Snackbar.make(signInBinding.root,"No Such User",Snackbar.LENGTH_SHORT).show()
            }
        }
    }


    private fun validateMPin(mPin: String): Boolean {
        return mPin.length == 4
    }

    private fun validateMobileNumber(text: String?): Boolean {
        val pattern = Pattern.compile("[6-9][0-9]{9}")
        val matcher = pattern.matcher(text)
        return matcher.matches()
    }
}