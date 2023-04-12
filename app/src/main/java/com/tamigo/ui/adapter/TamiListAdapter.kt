package com.tamigo.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.tamigo.R
import com.tamigo.databinding.ItemTamiListBinding

class TamiListAdapter(
    private val listTami: List<Int>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<TamiListAdapter.TamiListViewHolder>() {

    private var selectedItem = MutableLiveData<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TamiListViewHolder {
        val binding = ItemTamiListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TamiListViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: TamiListViewHolder, position: Int) {
        holder.bind(listTami[position])
        holder.itemView.setOnClickListener {
            onItemClick(listTami[position])
            selectedItem.value = holder.adapterPosition
            notifyDataSetChanged()
        }
        if (selectedItem.value == position) {
            holder.itemView.setBackgroundResource(R.drawable.ui_background_border)
        } else
            holder.itemView.setBackgroundColor(Color.TRANSPARENT)
    }

    override fun getItemCount(): Int {
        return listTami.size
    }

    class TamiListViewHolder(private val binding: ItemTamiListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Int) {
            binding.image.setImageResource(item)
        }
    }
}
