package com.buildbyhirenp.freshveggiemart.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.buildbyhirenp.freshveggiemart.R
import com.buildbyhirenp.freshveggiemart.activity.ProductDetailsActivity
import com.buildbyhirenp.freshveggiemart.adapter.AdapterProduct
import com.buildbyhirenp.freshveggiemart.databinding.FragmentSearchBinding
import com.buildbyhirenp.freshveggiemart.databinding.RawItemProductListBinding
import com.buildbyhirenp.freshveggiemart.models.Product
import com.buildbyhirenp.freshveggiemart.roomdb.CartProducts
import com.buildbyhirenp.freshveggiemart.utility.Utility
import com.buildbyhirenp.freshveggiemart.viewmodels.UserViewModel
import kotlinx.coroutines.launch

class FragmentSearch : Fragment() {

    lateinit var binding : FragmentSearchBinding
    private val viewModel : UserViewModel by viewModels()
    lateinit var adapterProduct: AdapterProduct

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        getAllProducts()
        searchProduct()

        return binding.root
    }

    private fun searchProduct() {
        binding.fragmentSearchEdittextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()
                Log.e("TAG", "onTextChanged: ===============" + s.toString().trim() )
                adapterProduct.filter.filter(query)
            }

            override fun afterTextChanged(s: Editable?) {}

        })
    }

    private fun getAllProducts() {
        binding.fragmentSearchShimmerLayout.visibility = View.VISIBLE
        lifecycleScope.launch {
            viewModel.fetchAllProduct().collect{

                if (it.isEmpty()){
                    binding.fragmentSearchRecyclerProducts.visibility = View.GONE
                    binding.fragmentSearchEmptyProductList.visibility = View.VISIBLE
                }else{
                    binding.fragmentSearchRecyclerProducts.visibility = View.VISIBLE
                    binding.fragmentSearchEmptyProductList.visibility = View.GONE
                }

                adapterProduct = AdapterProduct(::onAddButtonClick, ::onProductDetailClick)
                binding.fragmentSearchRecyclerProducts.adapter = adapterProduct
                adapterProduct.differ.submitList(it)
                adapterProduct.originalList = it as ArrayList<Product>

                binding.fragmentSearchShimmerLayout.visibility = View.GONE
            }
        }
    }

    fun onAddButtonClick(product: Product, binding : RawItemProductListBinding){

        lifecycleScope.launch {
            saveProductInRoomDB(product)
        }
    }

    fun onProductDetailClick(product: Product, binding : RawItemProductListBinding){
        val bundle = Bundle()

        bundle.putString("PROductID", product.productRandomId)
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

    private fun saveProductInRoomDB(product: Product) {

        val cartProduct = CartProducts(
            productID = product.productRandomId!!,
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