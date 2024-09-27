package com.buildbyhirenp.freshveggiemart.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.buildbyhirenp.freshveggiemart.R
import com.buildbyhirenp.freshveggiemart.activity.MainActivity
import com.buildbyhirenp.freshveggiemart.databinding.FragmentOTPBinding
import com.buildbyhirenp.freshveggiemart.models.Users
import com.buildbyhirenp.freshveggiemart.utility.FirebaseUtils
import com.buildbyhirenp.freshveggiemart.utility.Utility
import com.buildbyhirenp.freshveggiemart.viewmodels.AuthViewModel
import com.buildbyhirenp.freshveggiemart.viewmodels.UserViewModel
import kotlinx.coroutines.launch

class OTPFragment : Fragment() {

    private lateinit var binding: FragmentOTPBinding
    private val viewModel : AuthViewModel by viewModels()
    private val userviewModel : UserViewModel by viewModels()

    lateinit var mUtility : Utility
    lateinit var otp : String
    lateinit var Phonenumber : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mActivity = activity
        mUtility = Utility(mActivity!!)
        val bundle = arguments
        Phonenumber = bundle?.getString("Phone_number").toString()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentOTPBinding.inflate(inflater, container, false)

        binding.fragmentOtpTextviewPhonenumber.setText(Phonenumber)
        sendOTP()
        OnSubmitbtnClick()

        return binding.root
    }

    fun OnSubmitbtnClick() {
        binding.fragmentOtpButtonSubmit.setOnClickListener {
            //collect otp from all the edit texts
            Utility.showDialog(requireContext(), "Sign in.......")
            var EnterdOTP : String = binding.fragmentOtpEditTextOTP.text.toString()

            if (EnterdOTP.isEmpty() && EnterdOTP != null){
                Toast.makeText(context, "Enter the OTP", Toast.LENGTH_SHORT).show()
                Utility.hideDialog()
                return@setOnClickListener
            }

            verifyOTP(EnterdOTP)
        }
    }

    private fun verifyOTP(enterdOTP: String) {

        viewModel.signInWithPhoneAuthCredential(enterdOTP, Phonenumber, requireContext())

        lifecycleScope.launch {
            viewModel.isSignInSuccessfully.collect{
                if (it){
                    Utility.hideDialog()
                    sendToMain()
//                    val bundle = Bundle()
//                    bundle.putString("Phone_number" , Phonenumber)
//                    findNavController().navigate(R.id.action_OTPFragment_to_profileDetailsFragment, bundle)
                }
            }
        }
    }

    private fun sendToMain(){
        val users = Users(uid = FirebaseUtils.getCurrentUserID(), userPhoneNumber = Phonenumber, userAddress = "Click  \"+\"  to add the address")

        FirebaseUtils.currentUserDetails()!!.setValue(users)
            .addOnCompleteListener {

                if (it.isSuccessful) {
                    userviewModel.storemobile(Phonenumber)
                    startActivity(Intent(requireActivity(), MainActivity::class.java))
                    Utility.showToast(requireContext(), "Login Successfull")
                    requireActivity().finish()
                } else {
                    Log.e("TAG", "setUserName: =====error on navigate to main activity===")
                }
            }
    }

    fun sendOTP(){
        Utility.showDialog(requireContext(), "Sending OTP.......")
        viewModel.apply {
            viewModel.sendOTP(Phonenumber, requireActivity())
            lifecycleScope.launch {
                otpsend.collect{
                    if (it){
                        Utility.hideDialog()
                        Utility.showToast(requireContext(), "OTP Send.......")
                    }
                }
            }
        }
    }


}