package com.theberdakh.suvchiadmin.ui.farmers.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.theberdakh.suvchiadmin.R
import com.theberdakh.suvchiadmin.data.remote.farmers.models.Farmer
import com.theberdakh.suvchiadmin.databinding.ItemListFarmerBinding


class FarmersAdapter(val listener: FarmerClickEvent): PagingDataAdapter<Farmer, FarmersAdapter.FarmersViewHolder>(FarmersDiffCallback()) {
    interface FarmerClickEvent {
        fun onClick(farmer: Farmer)
    }

    class FarmersDiffCallback: DiffUtil.ItemCallback<Farmer>() {
        override fun areItemsTheSame(oldItem: Farmer, newItem: Farmer): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Farmer, newItem: Farmer): Boolean {
            return oldItem == newItem
        }
    }

    inner class FarmersViewHolder(private val binding: ItemListFarmerBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(farmer: Farmer){
            binding.tvFarmerName.text = binding.root.context.getString(R.string.full_name, farmer.firstName, farmer.lastName)
            binding.tvFarmerPhone.text = binding.root.context.getString(R.string.phone_number, farmer.phone)
            binding.root.setOnClickListener {
                listener.onClick(farmer)
            }

        }
    }



    override fun onBindViewHolder(holder: FarmersViewHolder, position: Int) = holder.bind(getItem(position)!!)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FarmersViewHolder {
        return FarmersViewHolder(
            ItemListFarmerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

}

