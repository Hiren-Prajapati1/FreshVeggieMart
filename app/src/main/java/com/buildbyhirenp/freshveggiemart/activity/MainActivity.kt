package com.buildbyhirenp.freshveggiemart.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.buildbyhirenp.freshveggiemart.R
import com.buildbyhirenp.freshveggiemart.databinding.ActivityMainBinding
import com.buildbyhirenp.freshveggiemart.databinding.DialogAddressLayoutBinding
import com.buildbyhirenp.freshveggiemart.fragment.FragmentCategory
import com.buildbyhirenp.freshveggiemart.fragment.FragmentHome
import com.buildbyhirenp.freshveggiemart.fragment.FragmentOrders
import com.buildbyhirenp.freshveggiemart.fragment.FragmentFavourite
import com.buildbyhirenp.freshveggiemart.fragment.FragmentSearch
import com.buildbyhirenp.freshveggiemart.models.Orders
import com.buildbyhirenp.freshveggiemart.utility.FirebaseUtils
import com.buildbyhirenp.freshveggiemart.utility.Utility
import com.buildbyhirenp.freshveggiemart.viewmodels.UserViewModel
import com.google.android.material.navigation.NavigationBarView
import com.google.type.Color
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private val viewModel: UserViewModel by viewModels()

    private var backPressedOnce = false
    private val exitToast by lazy {
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setAddress()

        val goSearch = intent.getBooleanExtra("goSearch", false)
        val goOrder = intent.getBooleanExtra("goOrder", false)

        binding.mainActivityCardviewDrawer.setOnClickListener {
            if (binding.container.isDrawerOpen) {
                binding.container.closeDrawer()
            } else {
                binding.container.openDrawer()
            }
        }

        if (goSearch){
            replaceFragment(FragmentSearch())
            binding.bottomNavigationView.selectedItemId = R.id.search
            binding.mainActivityAppbarDrawer.visibility = View.GONE
            binding.bottomNavigationView.setBackground(null)
        }else if (goOrder){
            val fragment = FragmentOrders()
            val bundle = Bundle()
            bundle.putBoolean("lottieAnimation", true)
            fragment.arguments = bundle

            replaceFragment(fragment)
            binding.bottomNavigationView.selectedItemId = R.id.order
            binding.mainActivityAppbarDrawer.visibility = View.GONE
            binding.bottomNavigationView.setBackground(null)
        }else{
            replaceFragment(FragmentHome())
            binding.bottomNavigationView.setBackground(null)
        }

        lifecycleScope.launch {
            binding.bottomNavigationView.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item: MenuItem ->
                if (item.itemId == R.id.home) {
                    replaceFragment(FragmentHome())
                    binding.mainActivityAppbarDrawer.visibility = View.VISIBLE
                } else if (item.itemId == R.id.search) {
                    replaceFragment(FragmentSearch())
                    binding.mainActivityAppbarDrawer.visibility = View.GONE
                } else if (item.itemId == R.id.order) {
                    replaceFragment(FragmentOrders())
                    binding.mainActivityAppbarDrawer.visibility = View.GONE
                } else if (item.itemId == R.id.favourite) {
                    replaceFragment(FragmentFavourite())
                    binding.mainActivityAppbarDrawer.visibility = View.GONE
                }
                true
            })
        }

        binding.mainActivityTextviewSearch.setOnClickListener {
            replaceFragment(FragmentSearch())
            binding.bottomNavigationView.selectedItemId = R.id.search
            binding.mainActivityAppbarDrawer.visibility = View.GONE
        }

        binding.fab.setOnClickListener {
            val i = Intent(this, SecondActivity::class.java)
            startActivity(i)
            overridePendingTransition(R.anim.fade_start, R.anim.fade_exit)
        }

        binding.activityMainLogout.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("LogOut!")
            builder.setMessage("Are you sure you want to LogOut?")

            // Add the buttons
            builder.setPositiveButton("Yes") { dialog, which ->
                FirebaseUtils.Logout()
                startActivity(Intent(this, AuthActivity::class.java))
                overridePendingTransition(R.anim.fade_start, R.anim.fade_exit)
            }

            builder.setNegativeButton("No") { dialog, which ->
                // Dismiss the dialog on negative button click
                dialog.dismiss()
            }

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

        binding.mainActivityCardviewAdmin.setOnClickListener {
            binding.mainActivityCardviewAdmin.backgroundTintList = ContextCompat.getColorStateList(this, R.color.mintgreen)
            binding.mainActivityCardviewApi.backgroundTintList = ContextCompat.getColorStateList(this, R.color.fade_gray)
            binding.textViewApiPan.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.textViewAdminPan.setTextColor(ContextCompat.getColor(this, R.color.white))
        }
        binding.mainActivityCardviewApi.setOnClickListener {
            binding.mainActivityCardviewAdmin.backgroundTintList = ContextCompat.getColorStateList(this, R.color.fade_gray)
            binding.mainActivityCardviewApi.backgroundTintList = ContextCompat.getColorStateList(this, R.color.mintgreen)
            binding.textViewApiPan.setTextColor(ContextCompat.getColor(this, R.color.white))
            binding.textViewAdminPan.setTextColor(ContextCompat.getColor(this, R.color.black))
        }
    }

    private fun setAddress() {

        viewModel.getUserAddress { address ->
            binding.mainActivityTextviewAddress.text = address.toString()

            if (address == "Click  \"+\"  to add the address"){
                binding.mainActivityEditAddress.setImageResource(R.drawable.ic_plus)
            }else{
                binding.mainActivityEditAddress.setImageResource(R.drawable.ic_edit)
            }
        }

        binding.mainActivityEditAddress.setOnClickListener {
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
                alertDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent) // Optional: Set background to transparent if needed
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

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(
            R.anim.fade_start,  // Enter animation
            R.anim.fade_exit, // Exit animation
            R.anim.fade_start,  // Pop enter animation (when returning to this fragment)
            R.anim.fade_exit  // Pop exit animation (when leaving this fragment)
        )
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }

    override fun onBackPressed() {
        if (backPressedOnce) {
            super.onBackPressed()
            val i = Intent(Intent.ACTION_MAIN)
            i.addCategory(Intent.CATEGORY_HOME)
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(i)
            return
        }

        backPressedOnce = true
        exitToast.show()

        Handler(Looper.getMainLooper()).postDelayed({
            backPressedOnce = false
        }, 2000)
    }
}