package com.theberdakh.suvchiadmin.ui.farmer_dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.patrykandpatrick.vico.core.chart.Chart
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.entriesOf
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.theberdakh.suvchiadmin.databinding.ItemListChartBinding

data class ChartItem(val title: String, val data: List<FloatEntry>)
object Charts {
    fun getCharts() = listOf(
        ChartItem(
            title = "Suw sarpı",
            data = entriesOf(4f, 12f, 8f, 16f, 13, 76, 45, 32, 14, 21, 33, 40)),
        ChartItem(
            title = "Suw tezligi",
            data = entriesOf(24f, 12f, 77f, 16f,44, 14, 35, 14, 64, 21, 33, 40)),

    )
}
class ChartAdapter: androidx.recyclerview.widget.ListAdapter<ChartItem, ChartAdapter.ChartViewHolder>(ChartItemDiffCallback()) {

    inner class ChartViewHolder(private val binding: ItemListChartBinding): ViewHolder(binding.root){
        fun bind(item: ChartItem) {
            val model = entryModelOf(item.data)
            binding.title.text = item.title
            binding.chartView.setModel(model)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChartViewHolder {
        return ChartViewHolder(ItemListChartBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: ChartViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}

class ChartItemDiffCallback: DiffUtil.ItemCallback<ChartItem>(){
    override fun areItemsTheSame(oldItem: ChartItem, newItem: ChartItem): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: ChartItem, newItem: ChartItem): Boolean {
        return oldItem == newItem
    }

}