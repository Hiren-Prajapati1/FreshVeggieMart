package com.buildbyhirenp.freshveggiemart.roomdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update


@Dao
interface CartProductsDao {

    // Cart Products Quary
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCartProduct(products: CartProducts)

    @Update
    fun UpdateCartProduct(products: CartProducts)

    @Query("SELECT * FROM CartProducts")
    fun getAllProducts() : LiveData<List<CartProducts>>

    @Query("DELETE FROM CartProducts WHERE productID = :productID")
    fun deleteCartProduct(productID : String)

    @Query("DELETE FROM CartProducts")
    fun deleteCartProducts()

    @Query("SELECT COUNT(*) FROM CartProducts WHERE productId = :id")
    fun isProductExist(id: String): LiveData<Int>

    // Favourite Products Quary
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavouriteProduct(products: FavouriteProducts)

    @Update
    fun UpdateFavouriteProduct(products: FavouriteProducts)

    @Query("SELECT * FROM FavouriteProducts")
    fun getAllFavouriteProducts() : LiveData<List<FavouriteProducts>>

    @Query("DELETE FROM FavouriteProducts WHERE productID = :productID")
    fun deleteFavouriteProduct(productID : String)

    @Query("SELECT COUNT(*) FROM FavouriteProducts WHERE productId = :id")
    fun isFabProductExist(id: String): LiveData<Int>
}