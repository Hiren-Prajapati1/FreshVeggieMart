package com.buildbyhirenp.freshveggiemart.roomdb

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.buildbyhirenp.freshveggiemart.utility.StringListTypeConverter

@Entity(tableName = "FavouriteProducts")
@TypeConverters(StringListTypeConverter::class)
data class FavouriteProducts(

    @PrimaryKey
    val productID: String = "random",

    var productTitle: String ? = null,
    var productQuantity: String ? = null,
    var productUnit: String ? = null,
    var productPrice: String ? = null,
    var productStock : Int? = null,
    var productCategory: String ? = null,
    var productType: String ? = null,
    var productDiscription: String ? = null,
    var adminUid : String? = null,
    var productImageUris: ArrayList<String?> ? = null
)
