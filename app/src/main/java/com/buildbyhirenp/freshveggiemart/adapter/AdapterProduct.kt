package com.buildbyhirenp.freshveggiemart.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.buildbyhirenp.freshveggiemart.databinding.RawItemProductListBinding
import com.buildbyhirenp.freshveggiemart.models.Product
import com.buildbyhirenp.freshveggiemart.utility.FilteringProducts
import com.denzcoskun.imageslider.models.SlideModel


class AdapterProduct(
    val onAddButtonClick: (Product, RawItemProductListBinding) -> Unit,
    val onProductDetailClick: (Product, RawItemProductListBinding) -> Unit
) : RecyclerView.Adapter<AdapterProduct.ProductViewHolder>(), Filterable {
    class ProductViewHolder (val binding : RawItemProductListBinding) : ViewHolder(binding.root) {}

    val diffutil = object : DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.productRandomId == newItem.productRandomId
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffutil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(RawItemProductListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
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

        }

    }

    private val filter : FilteringProducts? = null
    var originalList = ArrayList<Product>()
    override fun getFilter(): Filter {
        if (filter == null) return FilteringProducts(this, originalList)

        return filter
    }
}