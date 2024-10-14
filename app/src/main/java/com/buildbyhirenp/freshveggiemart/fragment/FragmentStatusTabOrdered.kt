package com.buildbyhirenp.freshveggiemart.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.buildbyhirenp.freshveggiemart.R
import com.buildbyhirenp.freshveggiemart.activity.SecondActivity
import com.buildbyhirenp.freshveggiemart.adapter.AdapterOrderHistory
import com.buildbyhirenp.freshveggiemart.databinding.FragmentStatusTabOrderedBinding
import com.buildbyhirenp.freshveggiemart.models.OrdersItem
import com.buildbyhirenp.freshveggiemart.viewmodels.UserViewModel
import kotlinx.coroutines.launch

class FragmentStatusTabOrdered : Fragment() {

    lateinit var binding : FragmentStatusTabOrderedBinding
    private val viewModel: UserViewModel by viewModels()
    lateinit var adapterOrderHistory: AdapterOrderHistory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentStatusTabOrderedBinding.inflate(inflater, container, false)

        getAllOrders()
        return binding.root
    }

    private fun getAllOrders() {
        binding.fragmentStatusOrderedShimmerLayout.visibility = View.VISIBLE
        lifecycleScope.launch {
            viewModel.getAllOrders().collect{orderList ->

                if (orderList.isNotEmpty()){
                    binding.fragmentStatusOrderedRecyclerProducts.visibility = View.VISIBLE
                    binding.fragmentStatusOrderedEmptyProductList.visibility = View.GONE

                    val orderedList = ArrayList<OrdersItem>()
                    for (order in orderList){

                        val title = StringBuilder()
                        var totlaPrice = 0

                        for (products in order.orderList!!){
                            val price = products.productPrice.toString().toInt()
                            val Itemcount = products.productCount
                            totlaPrice += (price.times(Itemcount!!))

                            title.append("${products.productCategory}, ")
                        }

                        if (order.orderStatus == 0){
                            val orededItems = OrdersItem(order.orderId, order.orderDate, order.orderStatus, title.toString(), totlaPrice)
                            orderedList.add(orededItems)
                        }

                    }

                    adapterOrderHistory = AdapterOrderHistory(requireContext(), ::onOrderedItemClick)
                    binding.fragmentStatusOrderedRecyclerProducts.adapter = adapterOrderHistory
                    adapterOrderHistory.differ.submitList(orderedList)

                    if (orderedList.isEmpty()){
                        binding.fragmentStatusOrderedEmptyProductList.visibility = View.VISIBLE
                    }else{
                        binding.fragmentStatusOrderedEmptyProductList.visibility = View.GONE
                    }

                    binding.fragmentStatusOrderedShimmerLayout.visibility = View.GONE
                }else{
                    binding.fragmentStatusOrderedRecyclerProducts.visibility = View.GONE
                    binding.fragmentStatusOrderedEmptyProductList.visibility = View.VISIBLE

                    binding.fragmentStatusOrderedShimmerLayout.visibility = View.GONE
                }
            }
        }
    }

    private fun onOrderedItemClick(oders : OrdersItem){
        val i = Intent(requireActivity(), SecondActivity::class.java)
        i.putExtra("status", oders.itemStatus)
        i.putExtra("orderId", oders.orderId)
        i.putExtra("orderPrice", oders.itemPrice)
        i.putExtra("goOrderDetails", true)
        startActivity(i)
        requireActivity().overridePendingTransition(R.anim.fade_start, R.anim.fade_exit)
    }

}