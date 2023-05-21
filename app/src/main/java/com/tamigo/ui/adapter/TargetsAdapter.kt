package com.tamigo.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tamigo.utils.data.Target
import com.tamigo.databinding.ItemTargetsBinding

class TargetsAdapter(
    private val listTargets: List<Target>,
    private val onItemClick: (Target) -> Unit
) : RecyclerView.Adapter<TargetsAdapter.TamiListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TamiListViewHolder {
        val binding = ItemTargetsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TamiListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TamiListViewHolder, position: Int) {
        holder.bind(listTargets[position]) {
            onItemClick(it)
        }
    }

    override fun getItemCount(): Int {
        return listTargets.size
    }

    class TamiListViewHolder(private val binding: ItemTargetsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Target, onClickChooseButton: (Target) -> Unit) {
            binding.prizeCoins.text = item.coins.toString()
            binding.numberSteps.text = item.steps.toString()
            binding.btnChoose.setOnClickListener {
                onClickChooseButton(item)
            }
        }
    }
}