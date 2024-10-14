package com.buildbyhirenp.freshveggiemart.auth

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.buildbyhirenp.freshveggiemart.R
import com.buildbyhirenp.freshveggiemart.activity.MainActivity
import com.buildbyhirenp.freshveggiemart.databinding.FragmentLoginBinding
import com.buildbyhirenp.freshveggiemart.databinding.FragmentOTPBinding
import com.buildbyhirenp.freshveggiemart.utility.Utility
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    lateinit var mUtility : Utility
    private var PhoneNumber : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mActivity = activity
        mUtility = Utility(mActivity!!)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.loginFragmentLottieAnimation.setAnimation(R.raw.background)
//        binding.loginFragmentLottieAnimation.playAnimation()

        binding.countryCodePicker.registerCarrierNumberEditText(binding.editTextNumber)

        binding.editTextNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!binding.countryCodePicker.isValidFullNumber){
                    binding.textInputLayoutNumber.error = "Please, Enter Valid Number"
                    binding.btnVerifyNumber.isEnabled = false
                    binding.btnVerifyNumber.setBackgroundResource(R.color.mintgreen)
                }else{
                    binding.textInputLayoutNumber.error = ""
                    binding.btnVerifyNumber.isEnabled = true
                    binding.btnVerifyNumber.setBackgroundResource(R.color.bg_color)
                }
            }
            override fun afterTextChanged(s: Editable) {}
        })

        binding.btnVerifyNumber.setOnClickListener {
            PhoneNumber = binding.countryCodePicker.selectedCountryCodeWithPlus.toString().trim() + binding.editTextNumber.text.toString().trim()

            Log.e("TAG", "onCreateView: =======1======" + PhoneNumber )

            if (binding.editTextNumber.text.toString().isEmpty()){
                val snackbar = Snackbar
                    .make(requireView(), "Please, enter Phone number", Snackbar.LENGTH_LONG)
                snackbar.show()
                return@setOnClickListener
            }

            OTPfragment()
        }

        return binding.root
    }

    fun OTPfragment() {
        PhoneNumber = binding.countryCodePicker.selectedCountryCodeWithPlus.toString().trim() + binding.editTextNumber.text.toString().trim()

        val bundle = Bundle()
        bundle.putString("Phone_number" , PhoneNumber)
        findNavController().navigate(R.id.action_loginFragment_to_OTPFragment , bundle)
        binding.progressBarPhonenumber.visibility = View.INVISIBLE
    }

}