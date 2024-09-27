package com.buildbyhirenp.freshveggiemart.utility

import com.buildbyhirenp.freshveggiemart.R

object constants {

    const val PREFS_NAME = "Fressveggimart_Preferences"
    const val AddressStatus = "Address_Status"
    const val UserMobile = "Phone_Number"

    const val MerchantID = "PGTESTPAYUAT"
    const val SaltKeys = "099eb0cd-02cf-4e2a-8aca-3e6c6aff0399"
    const val apiEndPoint: String = "/pg/v1/pay"
    const val merchantTransactionId = "txnId"

    // for product details
    const val PROductID = "PRODUCTID"
    const val PROductTitle = "PRODUCTTITLE"
    const val PROductQuantity = "PRODUCTQUNTITY"
    const val PROductUnit = "PRODUCTUNIT"
    const val PROductPrice = "PRODUCTPRICE"
    const val PROductStock = "PRODUCTSTOCK"
    const val PROductCategory = "PRODUCTCATEGORY"
    const val PROductType = "PRODUCTTYPE"
    const val PROductDiscription = "PRODUCTDISCRIPTION"
    const val ADminUid = "ADMINUID"
    const val PROductImageUris = "PRODUCTIMAGESLIST"

    val allProductCategory = arrayOf(
        "All",
        "Vegitables & Fruits",
        "Dairy & Breakfast",
        "Munchies",
        "Cold Drink & Juices",
        "Instant & Frozen Food",
        "Tea, Coffie & Health Drinks",
        "Bakery & Biscuits",
        "Sweet Tooth",
        "Atta Rice & Dal",
        "Dry Fruits Masala & Oil",
        "Sauces & Spreads",
        "Chicken Meat & Fish",
        "Pan Corner",
        "Organic & Premium",
        "Baby Care",
        "Pharma & Wellness",
        "Cleaning Essential",
        "Home & Office",
        "Personal Care",
        "Pet Care"
    )

    val allProductCategoryIcons = arrayOf(
        R.drawable.cat_all,
        R.drawable.cat_vegetable,
        R.drawable.cat_dairy_breakfast,
        R.drawable.cat_munchies,
        R.drawable.cat_cold_and_juices,
        R.drawable.cat_instant_frozen,
        R.drawable.cat_tea_coffee,
        R.drawable.cat_bakery_biscuits,
        R.drawable.cat_sweet_tooth,
        R.drawable.cat_atta_rice,
        R.drawable.cat_masala,
        R.drawable.cat_sauce_spreads,
        R.drawable.cat_chicken_meat,
        R.drawable.cat_paan_corner,
        R.drawable.cat_organic_premium,
        R.drawable.cat_baby_care,
        R.drawable.cat_pharma_wellness,
        R.drawable.cat_cleaning,
        R.drawable.cat_home_office,
        R.drawable.cat_personal_care,
        R.drawable.cat_pet_care,
    )
}