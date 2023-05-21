package com.tamigo.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tamigo.utils.data.FoodItem
import com.tamigo.databinding.ItemFoodBinding
import com.tamigo.databinding.ItemShopBinding

class FoodAdapter(
    private val listFood: MutableMap<Int, List<FoodItem>?>,
    private val onItemClick: (FoodItem) -> Unit
) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            SHOP_VIEW_HOLDER -> ShopViewHolder(
                ItemShopBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            INVENTORY_VIEW_HOLDER -> InventoryViewHolder(
                ItemFoodBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> InventoryViewHolder(
                ItemFoodBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listFood.values.first()
        if (item != null)
            when (holder) {
                is ShopViewHolder -> holder.bind(item[position]) {
                    onItemClick(it)
                }
                is InventoryViewHolder -> holder.bind(item[position]) {
                    onItemClick(it)
                }
            }
    }

    override fun getItemCount(): Int {
        val items = listFood.values.first()
        return if (items?.size != null)
            items.size
        else
            0
    }

    override fun getItemViewType(position: Int): Int {
        return listFood.keys.first()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(items: List<FoodItem>?) {
        listFood[INVENTORY_VIEW_HOLDER] = items
        notifyDataSetChanged()
    }

    class ShopViewHolder(private val binding: ItemShopBinding) :
        ViewHolder(binding.root) {
        fun bind(item: FoodItem, onItemClick: (FoodItem) -> Unit) {
            binding.iconFood.setImageResource(item.foodSrcId)
            binding.cost.text = item.cost.toString()
            binding.root.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    class InventoryViewHolder(private val binding: ItemFoodBinding) :
        ViewHolder(binding.root) {
        fun bind(item: FoodItem, onItemClick: (FoodItem) -> Unit) {
            binding.iconFood.setImageResource(item.foodSrcId)
            binding.root.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    companion object {
        const val SHOP_VIEW_HOLDER = 1
        const val INVENTORY_VIEW_HOLDER = 2
    }
}