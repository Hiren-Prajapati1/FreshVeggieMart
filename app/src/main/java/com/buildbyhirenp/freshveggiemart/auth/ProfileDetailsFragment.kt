package com.buildbyhirenp.freshveggiemart.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.buildbyhirenp.freshveggiemart.R
import com.buildbyhirenp.freshveggiemart.activity.MainActivity
import com.buildbyhirenp.freshveggiemart.databinding.FragmentOTPBinding
import com.buildbyhirenp.freshveggiemart.databinding.FragmentProfileDetailsBinding
import com.buildbyhirenp.freshveggiemart.models.Users
import com.buildbyhirenp.freshveggiemart.utility.FirebaseUtils
import com.buildbyhirenp.freshveggiemart.utility.Utility
import com.google.firebase.Timestamp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ProfileDetailsFragment : Fragment() {

    lateinit var binding : FragmentProfileDetailsBinding

    lateinit var mUtility : Utility
    lateinit var usermodel: Users

    lateinit var Username : String
    lateinit var Phonenumber : String
    lateinit var Address : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mActivity = activity
        mUtility = Utility(mActivity!!)
        val bundle = arguments
        Phonenumber = bundle?.getString("Phone_number").toString()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProfileDetailsBinding.inflate(inflater, container, false)

        binding.fragmentProfileDetailsPhonenumber.setText(Phonenumber)
        binding.fragmentProfileDetailsPhonenumber.isEnabled = false

        val focusListener =
            View.OnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    v.setBackgroundResource(R.drawable.border_active)
                } else {
                    v.setBackgroundResource(R.drawable.border)
                }
            }

        binding.fragmentProfileDetailsName.setOnFocusChangeListener(focusListener)
        binding.fragmentProfileDetailsAddress.setOnFocusChangeListener(focusListener)

        getUserName()

        binding.btnSubmit.setOnClickListener {
            setUserName()
        }

        return binding.root
    }

    private fun setUserName() {
//        Username = binding.fragmentProfileDetailsName.text.toString().trim()
//        Address = binding.fragmentProfileDetailsAddress.text.toString().trim()
//
//        if (Username.isEmpty()) {
//            Toast.makeText(context, "Please, enter your username", Toast.LENGTH_SHORT).show()
//            return
//        }
//        if (Address.isEmpty()) {
//            Toast.makeText(context, "Please, enter your Address", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        setInProgress(true)
//
//        if (usermodel == null) {
//            Log.e("TAG", "setUserName: ======usermodel is null; initializing new user====")
//            usermodel = Users(uid = FirebaseUtils.getCurrentUserID(), userName = Username, userPhoneNumber = Phonenumber, userAddress = Address)
//        } else {
//            Log.e("TAG", "setUserName: ======updating existing user====" + Phonenumber)
//            usermodel.userName = Username
//            usermodel.userPhoneNumber = Phonenumber
//            usermodel.userAddress = Address
//            usermodel.uid = FirebaseUtils.getCurrentUserID().toString()
//        }
//
//        FirebaseUtils.currentUserDetails()!!.setValue(usermodel)
//            .addOnCompleteListener {
//                setInProgress(false)
//                if (it.isSuccessful) {
//                    startActivity(Intent(requireActivity(), MainActivity::class.java))
//                    requireActivity().finish()
//                } else {
//                    Log.e("TAG", "setUserName: =====error on navigate to main activity===")
//                }
//            }
    }


    private fun getUserName() {
        setInProgress(true)

//        FirebaseUtils.currentUserDetails()!!.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if (snapshot.exists()) {
//                    val fetchedUserModel = snapshot.getValue(Users::class.java)
//                    if (fetchedUserModel != null) {
//                        usermodel = fetchedUserModel
//                        binding.fragmentProfileDetailsName.setText(usermodel.userName)
//                        binding.fragmentProfileDetailsAddress.setText(usermodel.userAddress)
//                    } else {
//                        Log.e("TAG", "getUserName: =====user Not registered====")
//                        usermodel = Users("", "", "", "")
//                    }
//                } else {
//                    Log.e("TAG", "getUserName: =====snapshot does not exist====")
//                    usermodel = Users("", "", "", "")
//                }
//                setInProgress(false) // Ensure you call this regardless of the result
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(context, "Get method Problem: ${error.message}", Toast.LENGTH_SHORT).show()
//                setInProgress(false) // Ensure you call this in case of error as well
//            }
//        })
    }

    private fun setInProgress(inProcess: Boolean) {
        if (inProcess){
            binding.progressBarProfileDetils.visibility = View.VISIBLE
            binding.btnSubmit.visibility = View.GONE
        }else{
            binding.progressBarProfileDetils.visibility = View.GONE
            binding.btnSubmit.visibility = View.VISIBLE
        }
    }

}