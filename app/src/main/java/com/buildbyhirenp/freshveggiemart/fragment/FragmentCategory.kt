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
import com.buildbyhirenp.freshveggiemart.activity.MainActivity
import com.buildbyhirenp.freshveggiemart.activity.ProductDetailsActivity
import com.buildbyhirenp.freshveggiemart.adapter.AdapterProduct
import com.buildbyhirenp.freshveggiemart.databinding.FragmentCategoryBinding
import com.buildbyhirenp.freshveggiemart.databinding.RawItemProductListBinding
import com.buildbyhirenp.freshveggiemart.models.Product
import com.buildbyhirenp.freshveggiemart.roomdb.CartProducts
import com.buildbyhirenp.freshveggiemart.utility.Utility
import com.buildbyhirenp.freshveggiemart.viewmodels.UserViewModel
import kotlinx.coroutines.launch

class FragmentCategory : Fragment() {

    lateinit var binding : FragmentCategoryBinding
    private val viewModel: UserViewModel by viewModels()

    lateinit var adapterProduct: AdapterProduct
    private var category : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        category = bundle?.getString("category")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCategoryBinding.inflate(layoutInflater, container, false)

        binding.fragmentCategoryToolbar.title = category

        fetchCategoryProduct()
        onSearchIconClick()
        onNavigationIconClick()

        return binding.root
    }

    private fun onNavigationIconClick() {
        binding.fragmentCategoryToolbar.setNavigationOnClickListener {
            requireActivity().finish()
            requireActivity().overridePendingTransition(R.anim.fade_start, R.anim.fade_exit)
        }
    }

    private fun onSearchIconClick() {
        binding.fragmentCategoryImageviewSearch.setOnClickListener {
            val i = Intent(requireActivity(), MainActivity::class.java)
            i.putExtra("goSearch", true)
            startActivity(i)
            requireActivity().overridePendingTransition(R.anim.fade_start, R.anim.fade_exit)
        }
    }

    private fun fetchCategoryProduct() {
        binding.fragmentCategoryShimmerLayout.visibility = View.VISIBLE
        lifecycleScope.launch {
            viewModel.getCategoryProduct(category!!).collect{

                if (it.isEmpty()){
                    binding.fragmentCategoryRecyclerProducts.visibility = View.GONE
                    binding.fragmentCategoryEmptyProductList.visibility = View.VISIBLE
                }else{
                    binding.fragmentCategoryRecyclerProducts.visibility = View.VISIBLE
                    binding.fragmentCategoryEmptyProductList.visibility = View.GONE
                }

                adapterProduct = AdapterProduct(::onAddButtonClick, ::onProductDetailClick)
                binding.fragmentCategoryRecyclerProducts.adapter = adapterProduct
                adapterProduct.differ.submitList(it)

                binding.fragmentCategoryShimmerLayout.visibility = View.GONE
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