package com.buildbyhirenp.freshveggiemart.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.buildbyhirenp.freshveggiemart.databinding.RawItemProductCategoryBinding
import com.buildbyhirenp.freshveggiemart.models.Category

class AdapterCategory(val categoryList: ArrayList<Category>, val onCategoryClick: (Category) -> Unit) : RecyclerView.Adapter<AdapterCategory.CategoryViewHolder>(){
    class CategoryViewHolder (val binding : RawItemProductCategoryBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(RawItemProductCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categoryList[position]
        holder.binding.apply {
            rawItemProductCategoryImage.setImageResource(category.image)
            rawItemProductCategoryName.text = category.title
        }

        holder.itemView.setOnClickListener {
            onCategoryClick(category)
        }
    }
}