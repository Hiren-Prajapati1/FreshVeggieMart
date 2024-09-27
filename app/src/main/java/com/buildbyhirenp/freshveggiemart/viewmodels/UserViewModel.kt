package com.buildbyhirenp.freshveggiemart.viewmodels

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.buildbyhirenp.freshveggiemart.models.Orders
import com.buildbyhirenp.freshveggiemart.models.Product
import com.buildbyhirenp.freshveggiemart.roomdb.CartProducts
import com.buildbyhirenp.freshveggiemart.roomdb.CartProductsDao
import com.buildbyhirenp.freshveggiemart.roomdb.CartProductsDatabase
import com.buildbyhirenp.freshveggiemart.roomdb.FavouriteProducts
import com.buildbyhirenp.freshveggiemart.utility.FirebaseUtils
import com.buildbyhirenp.freshveggiemart.utility.constants.AddressStatus
import com.buildbyhirenp.freshveggiemart.utility.constants.PREFS_NAME
import com.buildbyhirenp.freshveggiemart.utility.constants.UserMobile
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow

class UserViewModel(application: Application) : AndroidViewModel(application){

    //initialization
    val SharedPreference : SharedPreferences = application.getSharedPreferences(PREFS_NAME, 0)
    val cartProductDao : CartProductsDao = CartProductsDatabase.getDatabaseInstant(application).cartsProductDao()


    //Room DB
    // Cart Product
    suspend fun insertCartProduct(products: CartProducts){
        cartProductDao.insertCartProduct(products)
    }
    suspend fun updateCartProduct(products: CartProducts){
        cartProductDao.UpdateCartProduct(products)
    }
    suspend fun deleteCartProduct(productId : String){
        cartProductDao.deleteCartProduct(productId)
    }
    fun getAll() : LiveData<List<CartProducts>>{
        return cartProductDao.getAllProducts()
    }
    suspend fun deleteCartProducts(){
        cartProductDao.deleteCartProducts()
    }
    fun isCartProductExist(productId: String): LiveData<Int> {
        return cartProductDao.isProductExist(productId)
    }

    // Favourite Product DB
    suspend fun insertFavouriteProduct(products: FavouriteProducts){
        cartProductDao.insertFavouriteProduct(products)
    }
    suspend fun updateFavouriteProduct(products: FavouriteProducts){
        cartProductDao.UpdateFavouriteProduct(products)
    }
    suspend fun deleteFavouriteProduct(productId : String){
        cartProductDao.deleteFavouriteProduct(productId)
    }
    fun getAllFavouriteProducts() : LiveData<List<FavouriteProducts>>{
        return cartProductDao.getAllFavouriteProducts()
    }
    fun isFavouriteProductExist(productId: String): LiveData<Int> {
        return cartProductDao.isFabProductExist(productId)
    }


    // Firebase Call
    fun fetchAllProduct(): Flow<List<Product>> = callbackFlow {
        val db = FirebaseUtils.fetchProductPath("AllProduct")

        val eventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val products = ArrayList<Product>()

                for (product in snapshot.children){
                    val pro = product.getValue(Product::class.java)
                    products.add(pro!!)
                }
                trySend(products)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        db.addValueEventListener(eventListener)
        awaitClose{db.removeEventListener(eventListener)}
    }
    fun getAllOrders(): Flow<List<Orders>> = callbackFlow {
        val db = FirebaseDatabase.getInstance().getReference("Admins").child("Orders").orderByChild("orderStatus")

        val eventListerner = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val OrderHistoryList = ArrayList<Orders>()
                for (orders in snapshot.children){
                    val order = orders.getValue(Orders::class.java)
                    if (order?.orderingUserId == FirebaseUtils.getCurrentUserID()){
                        OrderHistoryList.add(order!!)
                    }
                }
                trySend(OrderHistoryList)
            }

            override fun onCancelled(error: DatabaseError) {}
        }
        db.addValueEventListener(eventListerner)
        awaitClose { db.removeEventListener(eventListerner) }
    }
    fun getCategoryProduct(category : String) : Flow<List<Product>> = callbackFlow{

        val db = FirebaseUtils.fetchProductPath("ProductCategory/${category}")

        val eventListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val products = ArrayList<Product>()

                for (product in snapshot.children){
                    val pro = product.getValue(Product::class.java)
                    products.add(pro!!)
                }
                trySend(products)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        db.addValueEventListener(eventListener)
        awaitClose{db.removeEventListener(eventListener)}
    }
    fun getTypeProduct(productType: String, ProductID: String?) : Flow<List<Product>> = callbackFlow{

        val db = FirebaseUtils.fetchProductPath("ProductType/${productType}")

        val eventListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val products = ArrayList<Product>()

                for (product in snapshot.children){

                    if (product.key != ProductID){
                        Log.e("TAG", "onDataChange: ============" + product )
                        val pro = product.getValue(Product::class.java)
                        products.add(pro!!)
                    }
                }
                trySend(products)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        db.addValueEventListener(eventListener)
        awaitClose{db.removeEventListener(eventListener)}
    }
    fun getOrderProducts(orderId : String) : Flow<List<CartProducts>> = callbackFlow {
        val db = FirebaseDatabase.getInstance().getReference("Admins").child("Orders").child(orderId)

        val eventListerner = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val order = snapshot.getValue(Orders::class.java)

                trySend(order?.orderList!!)
            }

            override fun onCancelled(error: DatabaseError) {}
        }
        db.addValueEventListener(eventListerner)
        awaitClose { db.removeEventListener(eventListerner) }
    }
    fun SaveUserAddress(address : String){
        FirebaseUtils.currentUserDetails()!!.child("userAddress").setValue(address)
    }
    fun getUserAddress(callBack : (String?) -> Unit){
        val db = FirebaseUtils.currentUserDetails()!!.child("userAddress")

        db.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val address = snapshot.getValue(String::class.java)
                    callBack(address)
                }else{
                    callBack(null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callBack(null)
            }

        })
    }
    fun saveOrderedProducts(orders: Orders){
        FirebaseDatabase.getInstance().getReference("Admins").child("Orders").child(orders.orderId!!).setValue(orders)
    }

    // check user address
    fun saveAddressStatus(){
        SharedPreference.edit().putBoolean(AddressStatus, true).apply()
    }
    fun getAddressStatus() : MutableLiveData<Boolean>{
        val status = MutableLiveData<Boolean>()
        status.value = SharedPreference.getBoolean(AddressStatus, false)
        return status
    }

    //store phone
    fun storemobile(number : String){
        SharedPreference.edit().putString(UserMobile, number).apply()
    }
    fun getMobile() : MutableLiveData<String>{
        val number = MutableLiveData<String>()
        number.value = SharedPreference.getString(UserMobile, "")
        return number
    }

}