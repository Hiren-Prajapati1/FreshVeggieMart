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
import com.buildbyhirenp.freshveggiemart.databinding.FragmentStatusTabDispatchBinding
import com.buildbyhirenp.freshveggiemart.models.OrdersItem
import com.buildbyhirenp.freshveggiemart.viewmodels.UserViewModel
import kotlinx.coroutines.launch

class FragmentStatusTabDispatch : Fragment() {

    lateinit var binding : FragmentStatusTabDispatchBinding
    private val viewModel: UserViewModel by viewModels()
    lateinit var adapterOrderHistory: AdapterOrderHistory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentStatusTabDispatchBinding.inflate(inflater, container, false)

        getAllOrders()
        return binding.root
    }

    private fun getAllOrders() {
        binding.fragmentStatusDispatchedShimmerLayout.visibility = View.VISIBLE
        lifecycleScope.launch {
            viewModel.getAllOrders().collect{orderList ->

                if (orderList.isNotEmpty()){
                    binding.fragmentStatusDispatchedRecyclerProducts.visibility = View.VISIBLE
                    binding.fragmentStatusDispatchedEmptyProductList.visibility = View.GONE

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

                        if (order.orderStatus == 2){
                            val orededItems = OrdersItem(order.orderId, order.orderDate, order.orderStatus, title.toString(), totlaPrice)
                            orderedList.add(orededItems)
                        }

                    }

                    adapterOrderHistory = AdapterOrderHistory(requireContext(), ::onOrderedItemClick)
                    binding.fragmentStatusDispatchedRecyclerProducts.adapter = adapterOrderHistory
                    adapterOrderHistory.differ.submitList(orderedList)

                    binding.fragmentStatusDispatchedShimmerLayout.visibility = View.GONE
                }else{
                    binding.fragmentStatusDispatchedRecyclerProducts.visibility = View.GONE
                    binding.fragmentStatusDispatchedEmptyProductList.visibility = View.VISIBLE

                    binding.fragmentStatusDispatchedShimmerLayout.visibility = View.GONE
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