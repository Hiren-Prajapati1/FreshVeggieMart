package com.buildbyhirenp.freshveggiemart.roomdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CartProducts")
data class CartProducts(

    @PrimaryKey
    val productID: String = "random",

    var productTitle: String ? = null,
    var productQuantity: String ? = null,
    var productUnit: String ? = null,
    var productPrice: String? = null,
    var productCount: Int ? = null,
    var productStock: Int? = null,
    var productCategory: String ? = null,
    var productImage: String? = null,
    var adminUid: String? = null
)