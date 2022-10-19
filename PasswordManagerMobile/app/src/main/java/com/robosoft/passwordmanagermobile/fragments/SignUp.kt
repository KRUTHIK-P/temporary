package com.robosoft.passwordmanagermobile.fragments

import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.robosoft.fragmentlifecycle.Communicator
import com.robosoft.passwordmanagermobile.R
import com.robosoft.passwordmanagermobile.databinding.FragmentSignUpBinding
import com.robosoft.passwordmanagermobile.dataclass.User
import com.robosoft.passwordmanagermobile.db.UserDB
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class SignUp : Fragment() {

    private val DATABASE_NAME: String = "USER_DATABASE"

    private lateinit var signUpBinding: FragmentSignUpBinding

    private lateinit var userDB: UserDB

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        signUpBinding = FragmentSignUpBinding.inflate(inflater, container, false)

        signUpBinding.passwordToggleImageButton.setOnClickListener {

            if (signUpBinding.reEnterMPinEt.inputType == InputType.TYPE_CLASS_NUMBER) {
                signUpBinding.passwordToggleImageButton.setImageResource(R.drawable.ic_baseline_visibility_off_24)
                signUpBinding.reEnterMPinEt.inputType =
                    InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
            } else {
                signUpBinding.passwordToggleImageButton.setImageResource(R.drawable.ic_baseline_visibility_24)
                signUpBinding.reEnterMPinEt.inputType = InputType.TYPE_CLASS_NUMBER
            }
        }

        signUpBinding.signInBn.setOnClickListener {
            writeData()
        }

        return signUpBinding.root
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun writeData() {

        var mobileNumberEditTextFlag = false
        val mobileNumberEditText = signUpBinding.mobileNumberEt
        val mobileNumber = mobileNumberEditText.text.toString()

        if (!validateMobileNumber(mobileNumber)) {
            mobileNumberEditText.error = "Invalid Mobile Number"
        } else mobileNumberEditTextFlag = true


        var mPinEditTextFlag = false
        val mPinEditText = signUpBinding.mPinEt
        val mPin = mPinEditText.text.toString()

        if (!validateMPin(mPin)) {
            mPinEditText.error = "MPin should be 4 digits"
        } else mPinEditTextFlag = true


        var reEnterMPinEditTextFlag = false
        val reEnterMPinEditText = signUpBinding.reEnterMPinEt
        val reEnterMPin = reEnterMPinEditText.text.toString()

        if (mPin != reEnterMPin)
            reEnterMPinEditText.error = "Should be same as above MPin"
        else reEnterMPinEditTextFlag = true

        if (mobileNumberEditTextFlag and mPinEditTextFlag and reEnterMPinEditTextFlag) {
            val user = User(null, mobileNumber, mPin)

            GlobalScope.launch(Dispatchers.IO) {

                userDB = UserDB.getDatabase(signUpBinding.root.context)
                userDB.userDao().insert(user)
            }


            val communicator = activity as Communicator
            communicator.passControl()

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