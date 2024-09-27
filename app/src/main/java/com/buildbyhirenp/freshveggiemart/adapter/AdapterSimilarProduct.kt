package com.buildbyhirenp.freshveggiemart.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.buildbyhirenp.freshveggiemart.databinding.RawItemProductDetailsSimilarTypeBinding
import com.buildbyhirenp.freshveggiemart.databinding.RawItemProductListBinding
import com.buildbyhirenp.freshveggiemart.models.Product
import com.buildbyhirenp.freshveggiemart.roomdb.FavouriteProducts
import com.denzcoskun.imageslider.models.SlideModel
import kotlin.reflect.KFunction2


class AdapterSimilarProduct(
    val onProductDetailClick: KFunction2<Product, RawItemProductDetailsSimilarTypeBinding, Unit>
) : RecyclerView.Adapter<AdapterSimilarProduct.SimilarProductViewHolder>(){
    class SimilarProductViewHolder (val binding : RawItemProductDetailsSimilarTypeBinding) : ViewHolder(binding.root) {}

    val diffutil = object : DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.productRandomId == newItem.productRandomId
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffutil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilarProductViewHolder {
        return SimilarProductViewHolder(RawItemProductDetailsSimilarTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: SimilarProductViewHolder, position: Int) {
        val product = differ.currentList[position]

        holder.binding.apply {
            val imageList = ArrayList<SlideModel>()
            val productImage = product.productImageUris

            for (i in 0 until productImage?.size!!){
                imageList.add(SlideModel(product.productImageUris!![i].toString()))
            }

            rawProductDetailsSimilarTypeSliderImage.setImageList(imageList)
            rawProductDetailsSimilarTypeTextviewTitle.text = product.productTitle

            val price = "₹" + product.productPrice + " /" + product.productUnit
            rawProductDetailsSimilarTypeTextviewPrice.text = price

            rawProductDetailsSimilarTypeView.setOnClickListener {
                onProductDetailClick(product, this)
            }

        }

    }

}