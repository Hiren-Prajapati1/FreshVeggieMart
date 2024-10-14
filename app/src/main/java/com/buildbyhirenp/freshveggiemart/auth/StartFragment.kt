package com.buildbyhirenp.freshveggiemart.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.buildbyhirenp.freshveggiemart.R
import com.buildbyhirenp.freshveggiemart.databinding.FragmentStartBinding

class StartFragment : Fragment() {

    lateinit var binding : FragmentStartBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentStartBinding.inflate(layoutInflater, container, false)

        binding.startFragmentLottieAnimation.setAnimation(R.raw.background)
        binding.startFragmentLottieAnimation.playAnimation()

        binding.fragmentStartBtn.setOnClickListener {
            findNavController().navigate(R.id.action_startFragment_to_loginFragment)
        }
        return binding.root
    }

}