package com.theberdakh.suvchiadmin.ui.farmer_sensors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.theberdakh.suvchiadmin.R
import com.theberdakh.suvchiadmin.data.remote.farmers.models.Farmer
import com.theberdakh.suvchiadmin.databinding.FragmentFarmerSensorsBinding
import com.theberdakh.suvchiadmin.ui.add_sensor.AddSensorFragment
import com.theberdakh.suvchiadmin.ui.attach_sensor.AttachSensorFragment
import com.theberdakh.suvchiadmin.utils.addFragment

class FarmerSensorsFragment(val farmer: Farmer) : Fragment() {
    private var _binding: FragmentFarmerSensorsBinding? = null
    private val binding get() = checkNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFarmerSensorsBinding.inflate(inflater, container, false)

        initListeners()


        return binding.root
    }

    private fun initListeners() {


        binding.fabCreateNewSensor.setOnClickListener {
            addFragment(parentFragmentManager, R.id.fragment_parent_container, AttachSensorFragment(farmer))
        }

    }


    override fun onDestroyView() {
        _binding =null
        super.onDestroyView()
    }
}