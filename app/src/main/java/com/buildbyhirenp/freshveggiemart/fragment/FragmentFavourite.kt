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
import com.buildbyhirenp.freshveggiemart.activity.ProductDetailsActivity
import com.buildbyhirenp.freshveggiemart.adapter.AdapterCart
import com.buildbyhirenp.freshveggiemart.adapter.AdapterFavourite
import com.buildbyhirenp.freshveggiemart.adapter.AdapterProduct
import com.buildbyhirenp.freshveggiemart.databinding.FragmentFavouriteBinding
import com.buildbyhirenp.freshveggiemart.databinding.RawItemProductListBinding
import com.buildbyhirenp.freshveggiemart.models.Product
import com.buildbyhirenp.freshveggiemart.roomdb.CartProducts
import com.buildbyhirenp.freshveggiemart.roomdb.FavouriteProducts
import com.buildbyhirenp.freshveggiemart.utility.Utility
import com.buildbyhirenp.freshveggiemart.viewmodels.UserViewModel
import kotlinx.coroutines.launch

class FragmentFavourite : Fragment() {

    lateinit var binding : FragmentFavouriteBinding
    private val viewModel: UserViewModel by viewModels()
    lateinit var adapterProduct: AdapterFavourite

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFavouriteBinding.inflate(inflater, container, false)

        getAllFavouriteProduct()

        return binding.root
    }

    private fun getAllFavouriteProduct() {
        binding.fragmentFavouriteShimmerLayout.visibility = View.VISIBLE
        viewModel.getAllFavouriteProducts().observe(viewLifecycleOwner) { data ->

            if (data.isNotEmpty()){
                binding.fragmentFavouriteRecyclerProducts.visibility = View.VISIBLE
                binding.fragmentFavouriteEmptyProductList.visibility = View.GONE

                adapterProduct = AdapterFavourite(::onAddButtonClick, ::onProductDetailClick)
                binding.fragmentFavouriteRecyclerProducts.adapter = adapterProduct
                adapterProduct.differ.submitList(data)

                binding.fragmentFavouriteShimmerLayout.visibility = View.GONE
            }else{
                binding.fragmentFavouriteRecyclerProducts.visibility = View.GONE
                binding.fragmentFavouriteEmptyProductList.visibility = View.VISIBLE

                binding.fragmentFavouriteShimmerLayout.visibility = View.GONE

            }
        }
    }

    fun onAddButtonClick(product: FavouriteProducts, binding : RawItemProductListBinding){

        lifecycleScope.launch {
            saveProductInRoomDB(product)
        }
    }

    fun onProductDetailClick(product: FavouriteProducts, binding : RawItemProductListBinding){
        val bundle = Bundle()

        bundle.putString("PROductID", product.productID)
        bundle.putString("PROductTitle", product.productTitle)
        bundle.putString("PROductQuantity", product.productQuantity.toString())
        bundle.putString("PROductUnit", product.productUnit)
        bundle.putString("PROductPrice", product.productPrice.toString())
        bundle.putInt("PROductStock", product.productStock!!)
        bundle.putString("PROductCategory", product.productCategory)
        bundle.putString("PROductType", product.productType)
        bundle.putString("PROductDiscription", product.productDiscription)
        bundle.putString("ADminUid", product.adminUid)
        bundle.putStringArrayList("PROductImageUris", product.productImageUris)

        val intent = Intent(context, ProductDetailsActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
        requireActivity().overridePendingTransition(R.anim.fade_start, R.anim.fade_exit)
    }

    private fun saveProductInRoomDB(product: FavouriteProducts) {

        val cartProduct = CartProducts(
            productID = product.productID!!,
            productTitle = product.productTitle,
            productQuantity = product.productQuantity.toString(),
            productUnit = product.productUnit,
            productPrice = product.productPrice.toString(),
            productCount = 1,
            productStock = product.productStock,
            productCategory = product.productCategory,
            productImage = product.productImageUris?.get(0),
            adminUid = product.adminUid
        )

        lifecycleScope.launch {
            viewModel.insertCartProduct(cartProduct)
            Utility.showToast(requireContext(), "Product added to Cart")
        }
    }

}