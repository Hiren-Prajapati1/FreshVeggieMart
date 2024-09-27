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
import com.buildbyhirenp.freshveggiemart.roomdb.CartProducts
import com.bumptech.glide.Glide

class AdapterCart(
    val onIncrementBtnClick: (CartProducts, RawItemCartProductBinding) -> Unit,
    val onDecrimentBtnClick: (CartProducts, RawItemCartProductBinding) -> Unit
) : RecyclerView.Adapter<AdapterCart.CartViewHolder>() {
    class CartViewHolder (val binding : RawItemCartProductBinding) : ViewHolder(binding.root)


    val diffutil = object : DiffUtil.ItemCallback<CartProducts>(){
        override fun areItemsTheSame(oldItem: CartProducts, newItem: CartProducts): Boolean {
            return oldItem.productID == newItem.productID
        }

        override fun areContentsTheSame(oldItem: CartProducts, newItem: CartProducts): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffutil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        return CartViewHolder(RawItemCartProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val product = differ.currentList[position]

        holder.binding.apply {
            Glide.with(holder.itemView).load(product.productImage).into(rawItemCartImageview)
            rawItemCartTextviewTitle.text = product.productTitle
            rawItemCartTextviewQuantity.text = "₹" + product.productPrice + " /" + product.productQuantity + product.productUnit
            rawItemCartTextviewItemcount.text = product.productCount.toString()

            if (rawItemCartTextviewItemcount.text.toString() > "1"){
                rawItemCartImageviewDecriment.setImageResource(R.drawable.ic_minus)
            }else{
                rawItemCartImageviewDecriment.setImageResource(R.drawable.ic_delete)
            }

            rawItemCartImageviewIncriment.setOnClickListener {
                onIncrementBtnClick(product, this)
            }

            rawItemCartImageviewDecriment.setOnClickListener {
                onDecrimentBtnClick(product, this)
            }
        }

    }
}