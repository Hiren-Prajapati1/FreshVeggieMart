package com.buildbyhirenp.freshveggiemart.auth

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.buildbyhirenp.freshveggiemart.R
import com.buildbyhirenp.freshveggiemart.activity.MainActivity
import com.buildbyhirenp.freshveggiemart.databinding.FragmentSplaceScreenBinding
import com.buildbyhirenp.freshveggiemart.utility.FirebaseUtils

class SplaceScreenFragment : Fragment() {

    lateinit var binding: FragmentSplaceScreenBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSplaceScreenBinding.inflate(layoutInflater)
        statusbar()
        Handler(Looper.getMainLooper()).postDelayed({

            if(FirebaseUtils.isLoggedIn()){
                startActivity(Intent(requireActivity() , MainActivity::class.java))
            }else{
                findNavController().navigate(R.id.action_splaceScreenFragment_to_startFragment)
            }

        }, 3000)
        return binding.root
    }

    private fun statusbar() {
        activity?.window?.apply {
            val statusbarcolors = ContextCompat.getColor(requireContext(), R.color.bg_color);
            statusBarColor = statusbarcolors
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }
}