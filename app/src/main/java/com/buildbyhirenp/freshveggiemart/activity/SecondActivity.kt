package com.buildbyhirenp.freshveggiemart.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.buildbyhirenp.freshveggiemart.R
import com.buildbyhirenp.freshveggiemart.databinding.ActivitySecondBinding
import com.buildbyhirenp.freshveggiemart.fragment.FragmentCart
import com.buildbyhirenp.freshveggiemart.fragment.FragmentCategory
import com.buildbyhirenp.freshveggiemart.fragment.FragmentOrderDetails

class SecondActivity : AppCompatActivity() {

    lateinit var binding : ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.getStringExtra("category")
        val goCategory = intent.getBooleanExtra("goCategory", false)

        val orderDetailsOrderStatus = intent.getIntExtra("status", 0)
        val orderDetailsOrderId = intent.getStringExtra("orderId")
        val orderDetailsOrderPrice = intent.getIntExtra("orderPrice", 0)
        val goOrderDetails = intent.getBooleanExtra("goOrderDetails", false)

        if (goCategory){
            val fragment = FragmentCategory()
            val bundle = Bundle()
            bundle.putString("category", data)
            fragment.arguments = bundle

            replaceFragment(fragment)
        }else if (goOrderDetails){
            val fragment = FragmentOrderDetails()
            val bundle = Bundle()
            bundle.putInt("status", orderDetailsOrderStatus)
            bundle.putString("orderId", orderDetailsOrderId)
            bundle.putInt("orderPrice", orderDetailsOrderPrice)
            fragment.arguments = bundle

            replaceFragment(fragment)
        }else{
            replaceFragment(FragmentCart())
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(
            R.anim.fade_start,  // Enter animation
            R.anim.fade_exit, // Exit animation
            R.anim.fade_start,  // Pop enter animation (when returning to this fragment)
            R.anim.fade_exit  // Pop exit animation (when leaving this fragment)
        )
        fragmentTransaction.replace(R.id.cart_activity_frame_layout, fragment)
        fragmentTransaction.commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()

        finish()
        overridePendingTransition(R.anim.fade_start, R.anim.fade_exit)
    }
}