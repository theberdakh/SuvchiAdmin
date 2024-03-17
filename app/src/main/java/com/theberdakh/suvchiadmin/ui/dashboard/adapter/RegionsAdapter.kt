package com.theberdakh.suvchiadmin.ui.dashboard.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.theberdakh.suvchiadmin.data.remote.regions.models.Region
import com.theberdakh.suvchiadmin.databinding.ItemListRegionBinding

class RegionsAdapter(val listener: RegionClickEvent): PagingDataAdapter<Region, RegionsAdapter.RegionsViewHolder>(RegionsDiffCallback()) {
    interface RegionClickEvent {
        fun onClick(region: Region)
    }

   inner class RegionsViewHolder(private val binding: ItemListRegionBinding): RecyclerView.ViewHolder(binding.root) {
       fun bind(region: Region){
           binding.tvName.text = region.name
           binding.parentLayout.setOnClickListener {
               listener.onClick(region)
           }

       }
   }



    override fun onBindViewHolder(holder: RegionsViewHolder, position: Int) = holder.bind(region = getItem(position)!!)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegionsViewHolder {
        return RegionsViewHolder(ItemListRegionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

}

class RegionsDiffCallback: DiffUtil.ItemCallback<Region>() {
    override fun areItemsTheSame(oldItem: Region, newItem: Region): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Region, newItem: Region): Boolean {
        return oldItem == newItem
    }

}