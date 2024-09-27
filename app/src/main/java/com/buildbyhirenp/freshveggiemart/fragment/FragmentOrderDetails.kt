package com.buildbyhirenp.freshveggiemart.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.buildbyhirenp.freshveggiemart.R
import com.buildbyhirenp.freshveggiemart.adapter.AdapterOrderProduct
import com.buildbyhirenp.freshveggiemart.databinding.FragmentOrderDetailsBinding
import com.buildbyhirenp.freshveggiemart.viewmodels.UserViewModel
import kotlinx.coroutines.launch

class FragmentOrderDetails : Fragment() {

    lateinit var binding : FragmentOrderDetailsBinding
    private val viewModel: UserViewModel by viewModels()
    lateinit var adapterproduct : AdapterOrderProduct

    private var orderDetailsOrderStatus : Int? = null
    private var orderDetailsOrderId : String? = null
    private var orderDetailsOrderPrice : Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        orderDetailsOrderStatus = bundle?.getInt("status")
        orderDetailsOrderId = bundle?.getString("orderId")
        orderDetailsOrderPrice = bundle?.getInt("orderPrice")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentOrderDetailsBinding.inflate(inflater, container, false)

        settingStatus()
        onNavigationIconClick()
        lifecycleScope.launch {
            getOrderProducts()
        }

        binding.fragmentOrderDetailsTotalPrice.text = orderDetailsOrderPrice.toString()

        return binding.root
    }

    private suspend fun getOrderProducts() {
        viewModel.getOrderProducts(orderDetailsOrderId!!).collect{CartList ->
            adapterproduct = AdapterOrderProduct()
            binding.fragmentOrderDetailsRecyclerview.adapter = adapterproduct
            adapterproduct.differ.submitList(CartList)
        }
    }

    private fun onNavigationIconClick() {
        binding.fragmentOrderDetailsToolbar.setNavigationOnClickListener {
            requireActivity().finish()
            requireActivity().overridePendingTransition(R.anim.fade_start, R.anim.fade_exit)
        }
    }

    private fun settingStatus() {

        val statusToView = mapOf(
            0 to listOf(binding.fragmentOrderDetailsStatus1),
            1 to listOf(binding.fragmentOrderDetailsStatus1, binding.fragmentOrderDetailsStatus2, binding.view1),
            2 to listOf(binding.fragmentOrderDetailsStatus1, binding.fragmentOrderDetailsStatus2, binding.view1, binding.fragmentOrderDetailsStatus3, binding.view2),
            3 to listOf(binding.fragmentOrderDetailsStatus1, binding.fragmentOrderDetailsStatus2, binding.view1, binding.fragmentOrderDetailsStatus3, binding.view2, binding.fragmentOrderDetailsStatus4, binding.view3)
        )

        val viewToTint = statusToView.getOrDefault(orderDetailsOrderStatus, emptyList())

        for (view in viewToTint){
            view.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.blue)
        }

//        when(orderDetailsOrderStatus){
//            0 ->{
//                binding.fragmentOrderDetailsStatus1.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.orange)
//            }
//            1 ->{
//                binding.fragmentOrderDetailsStatus1.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.orange)
//                binding.fragmentOrderDetailsStatus2.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.yellow)
//                binding.view1.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.orange)
//            }
//            2->{
//                binding.fragmentOrderDetailsStatus1.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.orange)
//                binding.fragmentOrderDetailsStatus2.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.yellow)
//                binding.view1.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.orange)
//                binding.fragmentOrderDetailsStatus3.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.blue)
//                binding.view2.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.yellow)
//            }
//            3 ->{
//                binding.fragmentOrderDetailsStatus1.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.orange)
//                binding.fragmentOrderDetailsStatus2.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.yellow)
//                binding.view1.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.orange)
//                binding.fragmentOrderDetailsStatus3.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.blue)
//                binding.view2.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.yellow)
//                binding.fragmentOrderDetailsStatus4.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.bg_color)
//                binding.view3.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.blue)
//            }
//        }

    }


}