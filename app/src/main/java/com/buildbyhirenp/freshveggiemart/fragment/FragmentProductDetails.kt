package com.buildbyhirenp.freshveggiemart.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.buildbyhirenp.freshveggiemart.R
import com.buildbyhirenp.freshveggiemart.databinding.FragmentOrderDetailsBinding
import com.buildbyhirenp.freshveggiemart.databinding.FragmentProductDetailsBinding

class FragmentProductDetails : Fragment() {

    lateinit var binding: FragmentProductDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false)



        return binding.root
    }

}