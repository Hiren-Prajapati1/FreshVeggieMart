package com.buildbyhirenp.freshveggiemart.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.buildbyhirenp.freshveggiemart.databinding.RawItemProductListBinding
import com.buildbyhirenp.freshveggiemart.models.Product
import com.buildbyhirenp.freshveggiemart.roomdb.FavouriteProducts
import com.buildbyhirenp.freshveggiemart.utility.FilteringProducts
import com.denzcoskun.imageslider.models.SlideModel

class AdapterFavourite(
    val onAddButtonClick: (FavouriteProducts, RawItemProductListBinding) -> Unit,
    val onProductDetailClick: (FavouriteProducts, RawItemProductListBinding) -> Unit
) : RecyclerView.Adapter<AdapterFavourite.FavProductViewHolder>() {
    class FavProductViewHolder (val binding : RawItemProductListBinding) : ViewHolder(binding.root) {}

    val diffutil = object : DiffUtil.ItemCallback<FavouriteProducts>(){
        override fun areItemsTheSame(oldItem: FavouriteProducts, newItem: FavouriteProducts): Boolean {
            return oldItem.productID == newItem.productID
        }

        override fun areContentsTheSame(oldItem: FavouriteProducts, newItem: FavouriteProducts): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffutil)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavProductViewHolder {
        return FavProductViewHolder(RawItemProductListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: FavProductViewHolder, position: Int) {
        val product = differ.currentList[position]

        holder.binding.apply {
            val imageList = ArrayList<SlideModel>()
            val productImage = product.productImageUris

            for (i in 0 until productImage?.size!!){
                imageList.add(SlideModel(product.productImageUris!![i].toString()))
            }

            rawProductListSliderImage.setImageList(imageList)
            rawProductListTextviewTitle.text = product.productTitle

            val price = "₹" + product.productPrice + " /" + product.productUnit
            rawProductListTextviewPrice.text = price

            rawProductListImageviewAdd.setOnClickListener {
                onAddButtonClick(product, this)
            }

            rawItemProductView.setOnClickListener {
                onProductDetailClick(product, this)
            }

            cardView.visibility = View.VISIBLE

        }

    }

}