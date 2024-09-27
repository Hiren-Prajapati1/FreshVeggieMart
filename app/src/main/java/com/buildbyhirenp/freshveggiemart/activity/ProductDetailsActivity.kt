package com.buildbyhirenp.freshveggiemart.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.buildbyhirenp.freshveggiemart.R
import com.buildbyhirenp.freshveggiemart.adapter.AdapterSimilarProduct
import com.buildbyhirenp.freshveggiemart.databinding.ActivityProductDetailsBinding
import com.buildbyhirenp.freshveggiemart.databinding.RawItemProductDetailsSimilarTypeBinding
import com.buildbyhirenp.freshveggiemart.models.Product
import com.buildbyhirenp.freshveggiemart.roomdb.CartProducts
import com.buildbyhirenp.freshveggiemart.roomdb.FavouriteProducts
import com.buildbyhirenp.freshveggiemart.utility.Utility
import com.buildbyhirenp.freshveggiemart.viewmodels.UserViewModel
import com.denzcoskun.imageslider.models.SlideModel
import kotlinx.coroutines.launch

class ProductDetailsActivity : AppCompatActivity() {

    lateinit var binding : ActivityProductDetailsBinding
    private val viewModel: UserViewModel by viewModels()
    lateinit var adapterProduct: AdapterSimilarProduct

    var ProductID : String? = null
    var ProductTitle : String? = null
    var ProductQuantity : String? = null
    var ProductUnit : String? = null
    var ProductPrice : String? = null
    var ProductStock : Int? = null
    var ProductCategory : String? = null
    var ProductType : String? = null
    var ProductDiscription : String? = null
    var AdminUid : String? = null
    var ProductImageUris : ArrayList<String?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getIntentData()
        setValueinUI()
        onAddCartBtnClick()
        checkProductExists()
        getAllSimilarProducts()

    }

    private fun getAllSimilarProducts() {
        lifecycleScope.launch {
            viewModel.getTypeProduct(ProductType!!, ProductID).collect{

                if (it.isEmpty()){
                    binding.activityProductDetailTextviewProduct.visibility = View.GONE
                }else{
                    binding.activityProductDetailTextviewProduct.visibility = View.VISIBLE
                }

                adapterProduct = AdapterSimilarProduct(::onProductDetailClick)
                binding.activityProductDetailRecyclerviewProduct.adapter = adapterProduct
                adapterProduct.differ.submitList(it)

            }
        }
    }

    fun onProductDetailClick(product: Product, binding : RawItemProductDetailsSimilarTypeBinding){
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

        val intent = Intent(this, ProductDetailsActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.fade_start, R.anim.fade_exit)
    }

    fun checkProductExists(){

        viewModel.isCartProductExist(ProductID!!).observe(this, Observer { count ->
            if (count > 0) {
                // Product exists
                binding.activityProductDetailBtnAddCart.text = "Go To Cart"
            } else {
                // Product does not exist
                binding.activityProductDetailBtnAddCart.text = "Add To Cart"
            }
        })

        viewModel.isFavouriteProductExist(ProductID!!).observe(this, Observer { count ->
            if (count > 0) {
                // Product exists
                binding.imgLike.setImageResource(R.drawable.ic_like)
            } else {
                // Product does not exist
                binding.imgLike.setImageResource(R.drawable.ic_unlike)
            }
        })

    }

    private fun onAddCartBtnClick() {
        binding.activityProductDetailBtnAddCart.setOnClickListener {

            if (binding.activityProductDetailBtnAddCart.text == "Add To Cart"){
                lifecycleScope.launch {
                    saveCartProductInRoomDB()
                }
            }else{
                val i = Intent(this, SecondActivity::class.java)
                startActivity(i)
                overridePendingTransition(R.anim.fade_start, R.anim.fade_exit)
            }
        }
    }

    private fun saveCartProductInRoomDB() {
        val cartProduct = CartProducts(
            productID = ProductID.toString(),
            productTitle = ProductTitle,
            productQuantity = ProductQuantity.toString(),
            productUnit = ProductUnit,
            productPrice = ProductPrice,
            productCount = 1,
            productStock = ProductStock,
            productCategory = ProductCategory,
            productImage = ProductImageUris?.get(0),
            adminUid = AdminUid
        )

        lifecycleScope.launch {
            viewModel.insertCartProduct(cartProduct)
            Utility.showToast(this@ProductDetailsActivity, "Product added to Cart")
        }
    }

    private fun getIntentData() {
        ProductID = intent.getStringExtra("PROductID")
        ProductTitle = intent.getStringExtra("PROductTitle")
        ProductQuantity = intent.getStringExtra("PROductQuantity")
        ProductUnit = intent.getStringExtra("PROductUnit")
        ProductPrice = intent.getStringExtra("PROductPrice")
        ProductStock = intent.getIntExtra("PROductStock", 0)
        ProductCategory = intent.getStringExtra("PROductCategory")
        ProductType = intent.getStringExtra("PROductType")
        ProductDiscription = intent.getStringExtra("PROductDiscription")
        AdminUid = intent.getStringExtra("ADminUid")
        ProductImageUris = intent.getStringArrayListExtra("PROductImageUris")
    }

    private fun setValueinUI() {
        lifecycleScope.launch {

            binding.activityProductDetailBack.setOnClickListener {
                finish()
                overridePendingTransition(R.anim.fade_start, R.anim.fade_exit)
            }

            binding.activityProductDetailLike.setOnClickListener {
                val likeDrawable = ContextCompat.getDrawable(this@ProductDetailsActivity, R.drawable.ic_like)

                if (binding.imgLike.drawable.constantState == likeDrawable?.constantState) {
                    // The ImageView has the "like" image
                    binding.imgLike.setImageResource(R.drawable.ic_unlike)
                    lifecycleScope.launch {
                        viewModel.deleteFavouriteProduct(ProductID.toString())
                    }
                } else {
                    // The ImageView does not have the "like" image
                    binding.imgLike.setImageResource(R.drawable.ic_like)
                    saveFavProductInRoomDB()
                }
            }

            binding.activityProductDetailShare.setOnClickListener {
                try {
                    val message = "*$ProductTitle*\n ₹$ProductPrice /$ProductUnit"
                    val intent = Intent(Intent.ACTION_VIEW)
                    val url = "https://api.whatsapp.com/send?phone=&text=" + Uri.encode(message)
                    intent.setPackage("com.whatsapp")
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                } catch (e: Exception) {
//                    Toast.makeText(this, "WhatsApp not installed.", Toast.LENGTH_SHORT).show()
                }
            }

            val imageList = ArrayList<SlideModel>()
            val productImage = ProductImageUris

            for (i in 0 until productImage?.size!!){
                imageList.add(SlideModel(ProductImageUris!![i].toString()))
            }

            binding.activityProductDetailImage.setImageList(imageList)

            binding.activityProductDetailTitle.text = ProductTitle.toString()
            binding.activityProductDetailPrice.text = "₹" + ProductPrice + " /" + ProductUnit

            binding.activityProductTvShortDescription.text = ProductDiscription
            binding.activityProductTvFullDescription.text = ProductDiscription

            binding.activityProductTvSeeMore.setOnClickListener {
                binding.activityProductTvShortDescription.visibility = View.GONE
                binding.activityProductTvFullDescription.visibility = View.VISIBLE
                binding.activityProductTvSeeMore.visibility = View.GONE
                binding.activityProductTvSeeLess.visibility = View.VISIBLE
            }

            binding.activityProductTvSeeLess.setOnClickListener {
                binding.activityProductTvShortDescription.visibility = View.VISIBLE
                binding.activityProductTvFullDescription.visibility = View.GONE
                binding.activityProductTvSeeMore.visibility = View.VISIBLE
                binding.activityProductTvSeeLess.visibility = View.GONE
            }

        }
    }

    private fun saveFavProductInRoomDB() {
        val favProduct = FavouriteProducts(
            productID = ProductID.toString(),
            productTitle =  ProductTitle,
            productQuantity = ProductQuantity,
            productUnit = ProductUnit,
            productPrice = ProductPrice,
            productStock = ProductStock,
            productCategory = ProductCategory,
            productType = ProductType,
            productDiscription = ProductDiscription,
            adminUid = AdminUid,
            productImageUris = ProductImageUris
        )

        lifecycleScope.launch {
            viewModel.insertFavouriteProduct(favProduct)
            Utility.showToast(this@ProductDetailsActivity, "Product Mark as Favourite")
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        finish()
        overridePendingTransition(R.anim.fade_start, R.anim.fade_exit)
    }
}