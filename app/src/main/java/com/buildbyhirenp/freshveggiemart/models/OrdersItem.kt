package com.buildbyhirenp.freshveggiemart.models

import com.buildbyhirenp.freshveggiemart.roomdb.CartProducts

data class OrdersItem(
    val orderId : String? = null,
    val itemDate : String? = null,
    val itemStatus : Int? = null,
    val itemTitle : String? = null,
    val itemPrice : Int? = null
)
