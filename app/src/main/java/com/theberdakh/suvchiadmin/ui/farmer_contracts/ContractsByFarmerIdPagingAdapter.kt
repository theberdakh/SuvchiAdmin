package com.theberdakh.suvchiadmin.ui.farmer_contracts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.theberdakh.suvchiadmin.data.remote.contract.models.ContractByFarmerId
import com.theberdakh.suvchiadmin.databinding.ItemListContractBinding
import com.theberdakh.suvchiadmin.utils.convertDateTime

class ContractsByFarmerIdPagingAdapter(val listener: ContractByFarmerIdClickEvent): PagingDataAdapter<ContractByFarmerId, ContractsByFarmerIdPagingAdapter.ContractsByFarmerIdViewHolder>(ContractByFarmerIdDiffCallback()) {
    interface ContractByFarmerIdClickEvent {
        fun onClick(contract: ContractByFarmerId)
    }

    inner class ContractsByFarmerIdViewHolder(private val binding: ItemListContractBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(contract: ContractByFarmerId){
            binding.textviewFileTitle.text = contract.contract.title
            binding.textviewFileSubtitle.text = convertDateTime(contract.contract.createdAt)
            binding.root.setOnClickListener {
                listener.onClick(contract)
            }

        }
    }



    override fun onBindViewHolder(holder: ContractsByFarmerIdViewHolder, position: Int) = holder.bind(contract = getItem(position)!!)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContractsByFarmerIdViewHolder {
        return ContractsByFarmerIdViewHolder(
            ItemListContractBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ))
    }

}

class ContractByFarmerIdDiffCallback: DiffUtil.ItemCallback<ContractByFarmerId>() {
    override fun areItemsTheSame(oldItem: ContractByFarmerId, newItem: ContractByFarmerId): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ContractByFarmerId, newItem: ContractByFarmerId): Boolean {
        return oldItem == newItem
    }

}