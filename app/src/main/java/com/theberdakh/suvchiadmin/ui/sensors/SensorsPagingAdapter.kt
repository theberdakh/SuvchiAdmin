package com.theberdakh.suvchiadmin.ui.sensors

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.theberdakh.suvchiadmin.data.remote.sensors.models.Sensor
import com.theberdakh.suvchiadmin.databinding.ItemListSensorBinding

class SensorsPagingAdapter(val listener: SensorClickEvent): PagingDataAdapter<Sensor, SensorsPagingAdapter.SensorsViewHolder>(SensorDiffCallback()){

    interface SensorClickEvent {
        fun onSensorClick(sensor: Sensor)
    }

    inner class SensorsViewHolder(private val binding: ItemListSensorBinding): ViewHolder(binding.root){
        fun bind(sensor: Sensor){
            binding.textviewSensorTitle.text = sensor.name
            binding.textviewSensorSubtitle.text = sensor.imei
            binding.root.setOnClickListener {
                listener.onSensorClick(sensor)
            }
        }
    }

    override fun onBindViewHolder(holder: SensorsViewHolder, position: Int) = holder.bind(getItem(position)!!)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SensorsViewHolder {
        return SensorsViewHolder(
            ItemListSensorBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


}

class SensorDiffCallback: DiffUtil.ItemCallback<Sensor>(){
    override fun areItemsTheSame(oldItem: Sensor, newItem: Sensor): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Sensor, newItem: Sensor): Boolean {
      return oldItem == newItem
    }

}