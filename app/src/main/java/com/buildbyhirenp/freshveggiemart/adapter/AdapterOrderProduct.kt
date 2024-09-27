package com.buildbyhirenp.freshveggiemart.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.buildbyhirenp.freshveggiemart.R
import com.buildbyhirenp.freshveggiemart.databinding.RawItemCartProductBinding
import com.buildbyhirenp.freshveggiemart.databinding.RawItemOrderProductsBinding
import com.buildbyhirenp.freshveggiemart.roomdb.CartProducts
import com.bumptech.glide.Glide

class AdapterOrderProduct() : RecyclerView.Adapter<AdapterOrderProduct.OrderViewHolder>() {
    class OrderViewHolder (val binding : RawItemOrderProductsBinding) : ViewHolder(binding.root)


    val diffutil = object : DiffUtil.ItemCallback<CartProducts>(){
        override fun areItemsTheSame(oldItem: CartProducts, newItem: CartProducts): Boolean {
            return oldItem.productID == newItem.productID
        }

        override fun areContentsTheSame(oldItem: CartProducts, newItem: CartProducts): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffutil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        return OrderViewHolder(RawItemOrderProductsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val product = differ.currentList[position]

        holder.binding.apply {
            Glide.with(holder.itemView).load(product.productImage).into(rawItemOrderImageview)
            rawItemOrderTextviewTitle.text = product.productTitle
            rawItemOrderTextviewPrice.text = "₹" + product.productPrice + " /" + product.productQuantity + product.productUnit
            rawItemOrderTextviewQuantity.text = "Quntity : " + product.productCount.toString()
        }

    }

}