package com.buildbyhirenp.freshveggiemart.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.buildbyhirenp.freshveggiemart.R
import com.buildbyhirenp.freshveggiemart.activity.OrderPlaceActivity
import com.buildbyhirenp.freshveggiemart.adapter.AdapterCart
import com.buildbyhirenp.freshveggiemart.databinding.FragmentCartBinding
import com.buildbyhirenp.freshveggiemart.databinding.RawItemCartProductBinding
import com.buildbyhirenp.freshveggiemart.roomdb.CartProducts
import com.buildbyhirenp.freshveggiemart.utility.Utility
import com.buildbyhirenp.freshveggiemart.viewmodels.UserViewModel
import kotlinx.coroutines.launch

class FragmentCart : Fragment() {

    lateinit var binding : FragmentCartBinding
    private val viewModel: UserViewModel by viewModels()
    private lateinit var adapterCartProducts: AdapterCart


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)

        getAllCartProduct()
        onNavigationIconClick()

        binding.fragmentCartButtonOrder.setOnClickListener {
            startActivity(Intent(requireActivity(), OrderPlaceActivity::class.java))
            requireActivity().overridePendingTransition(R.anim.fade_start, R.anim.fade_exit)
        }

        return binding.root
    }

    private fun onNavigationIconClick() {
        binding.fragmentCartToolbar.setNavigationOnClickListener {
            requireActivity().finish()
            requireActivity().overridePendingTransition(R.anim.fade_start, R.anim.fade_exit)
        }
    }

    private fun getAllCartProduct() {
        viewModel.getAll().observe(viewLifecycleOwner) { data ->

            if (data.isNotEmpty()){
                binding.fragmentCartRecyclerview.visibility = View.VISIBLE
                binding.fragmentCartEmptyProductList.visibility = View.GONE
                binding.constraintLayout.visibility = View.VISIBLE

                adapterCartProducts = AdapterCart(::onIncrementBtnClick, ::onDecrimentBtnClick)
                binding.fragmentCartRecyclerview.adapter = adapterCartProducts
                adapterCartProducts.differ.submitList(data)

                var totlaPrice = 0

                for (products in data){
                    val price = products.productPrice.toString().toInt()
                    val Itemcount = products.productCount

                    totlaPrice += (price.times(Itemcount!!))
                }

                binding.fragmentCartSubtotalPrice.text = "₹ " + totlaPrice.toString()
            }else{
                binding.fragmentCartRecyclerview.visibility = View.GONE
                binding.fragmentCartEmptyProductList.visibility = View.VISIBLE

                binding.constraintLayout.visibility = View.GONE
            }
        }
    }

    private fun onDeleteButtonClick(products: CartProducts){
        lifecycleScope.launch {
            viewModel.deleteCartProduct(products.productID)
            Utility.showToast(requireContext(), "Remove Product from Cart")
        }
    }

    private fun onIncrementBtnClick(products: CartProducts, productBinding: RawItemCartProductBinding){
        var ItemCountInc = productBinding.rawItemCartTextviewItemcount.text.toString().toInt()
        ItemCountInc++

        UpdateProductCount(products, productBinding, ItemCountInc)

    }

    private fun onDecrimentBtnClick(products: CartProducts, productBinding: RawItemCartProductBinding){
        var ItemCountDec = productBinding.rawItemCartTextviewItemcount.text.toString().toInt()
        ItemCountDec--

        if (ItemCountDec == 0){

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Remove?")
            builder.setMessage("Are you Sure Remove this Product")
            builder.setIcon(android.R.drawable.ic_dialog_alert)


            builder.setPositiveButton("Yes"){dialogInterface, which ->
                onDeleteButtonClick(products)
            }

            builder.setNegativeButton("No"){dialogInterface, which ->

            }

            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()

        }else{
            UpdateProductCount(products, productBinding, ItemCountDec)
        }

    }

    private fun UpdateProductCount(products: CartProducts, productBinding: RawItemCartProductBinding, itemCount: Int) {

        val updateCartProduct = CartProducts(
            productID = products.productID,
            productTitle = products.productTitle,
            productQuantity = products.productQuantity.toString(),
            productUnit = products.productUnit,
            productPrice = products.productPrice.toString(),
            productCount = itemCount,
            productStock = products.productStock,
            productCategory = products.productCategory,
            productImage = products.productImage,
            adminUid = products.adminUid
        )
        lifecycleScope.launch {
            viewModel.updateCartProduct(updateCartProduct)
            productBinding.rawItemCartTextviewItemcount.text = itemCount.toString()
        }
    }

}