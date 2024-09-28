package com.buildbyhirenp.freshveggiemart.fragment

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.buildbyhirenp.freshveggiemart.R
import com.buildbyhirenp.freshveggiemart.activity.SecondActivity
import com.buildbyhirenp.freshveggiemart.adapter.AdapterOrderHistory
import com.buildbyhirenp.freshveggiemart.adapter.AdapterOrderStatusPager
import com.buildbyhirenp.freshveggiemart.databinding.FragmentOrdersBinding
import com.buildbyhirenp.freshveggiemart.models.OrdersItem
import com.buildbyhirenp.freshveggiemart.viewmodels.UserViewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class FragmentOrders : Fragment() {

    lateinit var binding: FragmentOrdersBinding

    private var tabTitle = mutableMapOf(
        "OR" to R.drawable.status_ordered,
        "RE" to R.drawable.status_received,
        "DIS" to R.drawable.status_dispatched,
        "DE" to R.drawable.status_delivered
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentOrdersBinding.inflate(inflater, container, false)

        setTabLayout()

        return binding.root
    }

    private fun setTabLayout() {
        val titles = ArrayList(tabTitle.keys)
        binding.fragmentOrderViewpager.adapter = AdapterOrderStatusPager(this)
        TabLayoutMediator(binding.fragmentOrderTablayout, binding.fragmentOrderViewpager){tab, position ->
            tab.text = titles[position]
        }.attach()

        tabTitle.values.forEachIndexed { index, imageResid ->
            val textview = LayoutInflater.from(requireContext()).inflate(R.layout.tab_titles, null) as TextView

            textview.setCompoundDrawablesWithIntrinsicBounds(0, 0, imageResid,0)
            textview.compoundDrawablePadding = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 4f, resources.displayMetrics
            ).roundToInt()

            binding.fragmentOrderTablayout.getTabAt(index)?.customView = textview
        }
    }

}