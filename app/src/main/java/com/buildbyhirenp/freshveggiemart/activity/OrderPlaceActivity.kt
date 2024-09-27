package com.buildbyhirenp.freshveggiemart.activity

import android.R
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.buildbyhirenp.freshveggiemart.adapter.AdapterOrderProduct
import com.buildbyhirenp.freshveggiemart.databinding.ActivityOrderPlaceBinding
import com.buildbyhirenp.freshveggiemart.databinding.DialogAddressLayoutBinding
import com.buildbyhirenp.freshveggiemart.models.Orders
import com.buildbyhirenp.freshveggiemart.utility.FirebaseUtils
import com.buildbyhirenp.freshveggiemart.utility.Utility
import com.buildbyhirenp.freshveggiemart.utility.constants.MerchantID
import com.buildbyhirenp.freshveggiemart.utility.constants.SaltKeys
import com.buildbyhirenp.freshveggiemart.utility.constants.apiEndPoint
import com.buildbyhirenp.freshveggiemart.utility.constants.merchantTransactionId
import com.buildbyhirenp.freshveggiemart.viewmodels.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener


class OrderPlaceActivity : AppCompatActivity(), PaymentResultWithDataListener {

    lateinit var binding : ActivityOrderPlaceBinding
    private val viewModel: UserViewModel by viewModels()
    private lateinit var adapterOrderProducts: AdapterOrderProduct
    var totlaPrice = 0
    var UserMobile : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderPlaceBinding.inflate(layoutInflater)
        setContentView(binding.root)



        getUserMobile()
        getAllCartProducts()
        onNavigationIconClick()
        onPlaceOrderClick()
    }

    private fun getUserMobile() {
        viewModel.getMobile().observe(this) { number ->
            UserMobile = number
        }
    }

    private fun onNavigationIconClick() {
        binding.orderPlaceToolbar.setNavigationOnClickListener {
            finish()
            overridePendingTransition(com.buildbyhirenp.freshveggiemart.R.anim.fade_start, com.buildbyhirenp.freshveggiemart.R.anim.fade_exit)
        }
    }

    private fun onPlaceOrderClick() {
        binding.orderPlaceButtonOrder.setOnClickListener {

            viewModel.getUserAddress { address ->
                if (address == " "){
                    val AddressLayout = DialogAddressLayoutBinding.inflate(LayoutInflater.from(this))

                    val alertDialog = AlertDialog.Builder(this)
                        .setView(AddressLayout.root)
                        .create()
                    alertDialog.show()

                    AddressLayout.dialogAddressLayoutBtnSave.setOnClickListener {
                        saveAddress(alertDialog, AddressLayout)
                    }

                    // Adjust the size and background of the AlertDialog
                    if (alertDialog.window != null) {
                        alertDialog.window!!.setLayout(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                        alertDialog.window!!.setBackgroundDrawableResource(R.color.transparent) // Optional: Set background to transparent if needed
                    }
                }else{
                    initPayment()
                }
            }
        }
    }

    private fun initPayment() {
        val price = totlaPrice.toString().toInt() * 100
        val activity: Activity = this
        val co = Checkout()

        Checkout.preload(this)
        co.setKeyID("rzp_test_x8Dx8GS6O6Cg6K")

        try {
            val options = JSONObject()
            options.put("name","FreshVeggieMart")
            options.put("description","Ecommerce")
            //You can omit the image option to fetch the image from the dashboard
            options.put("image","http://example.com/image/rzp.jpg")
            options.put("theme.color", "#4CAF50");
            options.put("currency","INR");
//            options.put("order_id", "order_DBJOWzybf0sJbb");
            options.put("amount",price.toString())//pass amount in currency subunits

            val retryObj = JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            val prefill = JSONObject()
            prefill.put("email","test@gmail.com")
            prefill.put("contact",UserMobile.toString())

            options.put("prefill",prefill)
            co.open(activity,options)
        }catch (e: Exception){
            Toast.makeText(activity,"Error in payment: "+ e.message,Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
        Utility.showToast(this, "Payment Success..")

        lifecycleScope.launch {
            saveProduct()
            viewModel.deleteCartProducts()
        }

        startActivity(Intent(this@OrderPlaceActivity, MainActivity::class.java))
    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
//        Utility.showToast(this, "Error : ${p1}")
    }

    private fun saveProduct() {
        viewModel.getAll().observe(this){cartProductList ->
            if (cartProductList.isNotEmpty()){
                viewModel.getUserAddress { address ->
                    val order = Orders(
                        orderId = FirebaseUtils.getRandomId(),
                        orderList = cartProductList,
                        userAddress = address,
                        orderStatus = 0,
                        orderDate = FirebaseUtils.getCurrentDate(),
                        orderingUserId = FirebaseUtils.getCurrentUserID()
                    )
                    viewModel.saveOrderedProducts(order)
                }
            }
        }
    }

    private fun saveAddress(alertDialog: AlertDialog, addressLayout: DialogAddressLayoutBinding) {
        val userPinCode = addressLayout.dialogAddressLayoutPincode.text.toString()
        val userPhoneNo = addressLayout.dialogAddressLayoutMobile.text.toString()
        val userState = addressLayout.dialogAddressLayoutState.text.toString()
        val userDistrict = addressLayout.dialogAddressLayoutDistrict.text.toString()
        val userAddress = addressLayout.dialogAddressLayoutAddress.text.toString()

        val address = "$userPinCode, $userDistrict($userState), $userAddress, $userPhoneNo"

        lifecycleScope.launch {
            viewModel.SaveUserAddress(address)
            viewModel.saveAddressStatus()
        }

        alertDialog.dismiss()
        Utility.showToast(this, "Address Saved..")
    }

    private fun getAllCartProducts() {
        viewModel.getAll().observe(this) { data ->
            adapterOrderProducts = AdapterOrderProduct()
            binding.orderPlaceRecyclerview.adapter = adapterOrderProducts
            adapterOrderProducts.differ.submitList(data)



            for (products in data){
                val price = products.productPrice.toString().toInt()
                val Itemcount = products.productCount

                totlaPrice += (price.times(Itemcount!!))
            }

            binding.orderPlaceTextviewSubtotal.text = "₹" + totlaPrice.toString()

            if (totlaPrice < 200){
                binding.orderPlaceTextviewDeliverycharge.text = "₹70"
                totlaPrice += 70
            }

            binding.orderPlaceTextviewTotal.text = "₹" + totlaPrice.toString()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }
}