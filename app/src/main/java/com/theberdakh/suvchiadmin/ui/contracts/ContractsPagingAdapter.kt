package com.theberdakh.suvchiadmin.ui.contracts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.theberdakh.suvchiadmin.data.remote.contract.models.Contract
import com.theberdakh.suvchiadmin.data.remote.regions.models.Region
import com.theberdakh.suvchiadmin.databinding.ItemListContractBinding
import com.theberdakh.suvchiadmin.databinding.ItemListRegionBinding
import com.theberdakh.suvchiadmin.utils.convertDateTime


class ContractsPagingAdapter(val listener: ContractClickEvent): PagingDataAdapter<Contract, ContractsPagingAdapter.ContractsViewHolder>(RegionsDiffCallback()) {
    interface ContractClickEvent {
        fun onClick(contract: Contract)
    }

    inner class ContractsViewHolder(private val binding: ItemListContractBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(contract: Contract){
            binding.textviewFileTitle.text = contract.title
            binding.textviewFileSubtitle.text = convertDateTime(contract.createdAt)
            binding.root.setOnClickListener {
                listener.onClick(contract)
            }

        }
    }



    override fun onBindViewHolder(holder: ContractsViewHolder, position: Int) = holder.bind(contract = getItem(position)!!)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContractsViewHolder {
        return ContractsViewHolder(
            ItemListContractBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

}

class RegionsDiffCallback: DiffUtil.ItemCallback<Contract>() {
    override fun areItemsTheSame(oldItem: Contract, newItem: Contract): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Contract, newItem: Contract): Boolean {
        return oldItem == newItem
    }

}