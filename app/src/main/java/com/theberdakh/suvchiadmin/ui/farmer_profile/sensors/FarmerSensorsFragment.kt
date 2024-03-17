package com.theberdakh.suvchiadmin.ui.farmer_profile.sensors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.theberdakh.suvchiadmin.data.remote.farmers.models.Farmer
import com.theberdakh.suvchiadmin.databinding.FragmentFarmerSensorsBinding

class FarmerSensorsFragment(val farmer: Farmer) : Fragment() {
    private var _binding: FragmentFarmerSensorsBinding? = null
    private val binding get() = checkNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFarmerSensorsBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onDestroyView() {
        _binding =null
        super.onDestroyView()
    }
}