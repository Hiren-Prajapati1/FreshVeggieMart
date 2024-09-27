package com.buildbyhirenp.freshveggiemart.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.buildbyhirenp.freshveggiemart.R
import com.buildbyhirenp.freshveggiemart.databinding.RawItemOrderlistHistoryBinding
import com.buildbyhirenp.freshveggiemart.models.OrdersItem

class AdapterOrderHistory(val context: Context, val onOrderedItemClick: (OrdersItem) -> Unit) : RecyclerView.Adapter<AdapterOrderHistory.OrderHistoryViewModel>() {
    class OrderHistoryViewModel (val binding : RawItemOrderlistHistoryBinding) : ViewHolder(binding.root)

    val diffutil = object : DiffUtil.ItemCallback<OrdersItem>(){
        override fun areItemsTheSame(oldItem: OrdersItem, newItem: OrdersItem): Boolean {
            return oldItem.orderId == newItem.orderId
        }

        override fun areContentsTheSame(oldItem: OrdersItem, newItem: OrdersItem): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffutil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHistoryViewModel {
        return OrderHistoryViewModel(RawItemOrderlistHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: OrderHistoryViewModel, position: Int) {
        val order = differ.currentList[position]

        holder.binding.apply {
            rawItemOrderHistoryTitle.text = order.itemTitle
            rawItemOrderHistoryDate.text = order.itemDate
            rawItemOrderHistoryPrice.text = "₹${order.itemPrice.toString()}"

            when(order.itemStatus){
                0 -> {
                    rawItemOrderHistoryStatus.text = "Ordered"
                    rawItemOrderHistoryStatus.backgroundTintList = ContextCompat.getColorStateList(context, R.color.orange)
                }1 -> {
                    rawItemOrderHistoryStatus.text = "Received"
                    rawItemOrderHistoryStatus.backgroundTintList = ContextCompat.getColorStateList(context, R.color.yellow)
                }2 -> {
                    rawItemOrderHistoryStatus.text = "Dispatched"
                    rawItemOrderHistoryStatus.backgroundTintList = ContextCompat.getColorStateList(context, R.color.blue)
                }3 -> {
                    rawItemOrderHistoryStatus.text = "Delivered"
                    rawItemOrderHistoryStatus.backgroundTintList = ContextCompat.getColorStateList(context, R.color.bg_color)
                }
            }
        }

        holder.itemView.setOnClickListener {
            onOrderedItemClick(order)
        }

    }
}