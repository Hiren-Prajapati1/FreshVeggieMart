package com.buildbyhirenp.freshveggiemart.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.buildbyhirenp.freshveggiemart.R
import com.buildbyhirenp.freshveggiemart.activity.SecondActivity
import com.buildbyhirenp.freshveggiemart.adapter.AdapterCategory
import com.buildbyhirenp.freshveggiemart.databinding.FragmentHomeBinding
import com.buildbyhirenp.freshveggiemart.models.Category
import com.buildbyhirenp.freshveggiemart.utility.constants

class FragmentHome : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        setAllCategory()

        return binding.root
    }

    private fun setAllCategory() {
        val categorylist = ArrayList<Category>()

        for (i in 0 until constants.allProductCategoryIcons.size){
            categorylist.add(Category(constants.allProductCategory[i], constants.allProductCategoryIcons[i]))
        }

        binding.fragmentHomeRecyclerviewCategory.adapter = AdapterCategory(categorylist, ::onCategoryClick)
    }

    fun onCategoryClick(category: Category){
        val i = Intent(requireActivity(), SecondActivity::class.java)
        i.putExtra("category", category.title)
        i.putExtra("goCategory", true)
        startActivity(i)
        requireActivity().overridePendingTransition(R.anim.fade_start, R.anim.fade_exit)
    }

}