package com.buildbyhirenp.freshveggiemart.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.buildbyhirenp.freshveggiemart.fragment.FragmentStatusTabDelivered
import com.buildbyhirenp.freshveggiemart.fragment.FragmentStatusTabDispatch
import com.buildbyhirenp.freshveggiemart.fragment.FragmentStatusTabOrdered
import com.buildbyhirenp.freshveggiemart.fragment.FragmentStatusTabReceived

class AdapterOrderStatusPager(fragment : Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> FragmentStatusTabOrdered()
            1 -> FragmentStatusTabReceived()
            2 -> FragmentStatusTabDispatch()
            else -> FragmentStatusTabDelivered()
        }
    }

}