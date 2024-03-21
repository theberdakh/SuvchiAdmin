package com.theberdakh.suvchiadmin.ui.all_coordination.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.theberdakh.suvchiadmin.data.remote.coordination.models.Coordination
import com.theberdakh.suvchiadmin.databinding.ItemListCoordinationBinding

class CoordinationAdapter: PagingDataAdapter<Coordination, CoordinationAdapter.CoordinationViewHolder>(
    CoordinationDiffCallback()
) {
    interface CoordinationClickEvent {
        fun onClick()
    }

    inner class CoordinationViewHolder(val binding: ItemListCoordinationBinding): ViewHolder(binding.root){
        fun bind(coordination: Coordination){
            val title = "H: ${coordination.h}"
            val subtitle = "Q: ${coordination.q}"
            binding.tvCoordinationTitle.text = title
            binding.tvCoordinationSubtitle.text = subtitle
        }
    }

    override fun onBindViewHolder(holder: CoordinationViewHolder, position: Int) = holder.bind(getItem(position)!!)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoordinationViewHolder {
      return CoordinationViewHolder(
          ItemListCoordinationBinding.inflate(
              LayoutInflater.from(parent.context),
              parent,
              false
          )
      )
    }

}
class CoordinationDiffCallback: DiffUtil.ItemCallback<Coordination>(){
    override fun areItemsTheSame(oldItem: Coordination, newItem: Coordination): Boolean {
        return  oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Coordination, newItem: Coordination): Boolean {
        return oldItem == newItem
    }

}