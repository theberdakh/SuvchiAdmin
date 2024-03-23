package com.theberdakh.suvchiadmin.ui.farmer_dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.theberdakh.suvchiadmin.data.remote.farmers.models.Farmer
import com.theberdakh.suvchiadmin.databinding.FragmentFarmerDashboardBinding

class FarmerDashboardFragment(farmer: Farmer) : Fragment() {
    private var _binding: FragmentFarmerDashboardBinding? = null
    private val binding get() = checkNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFarmerDashboardBinding.inflate(inflater, container, false)

        val adapter = ChartAdapter()
        binding.recyclerView.adapter = adapter
        adapter.submitList(Charts.getCharts())

        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}