package com.theberdakh.suvchiadmin.ui.farmer_sensors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.theberdakh.suvchiadmin.R
import com.theberdakh.suvchiadmin.data.remote.farmers.models.Farmer
import com.theberdakh.suvchiadmin.data.remote.sensors.models.Sensor
import com.theberdakh.suvchiadmin.data.remote.utils.isOnline
import com.theberdakh.suvchiadmin.databinding.FragmentFarmerSensorsBinding
import com.theberdakh.suvchiadmin.presentation.AdminViewModel
import com.theberdakh.suvchiadmin.ui.all_sensors.SensorsPagingAdapter
import com.theberdakh.suvchiadmin.ui.attach_sensor.AttachSensorFragment
import com.theberdakh.suvchiadmin.utils.addFragment
import com.theberdakh.suvchiadmin.utils.showToast
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FarmerSensorsFragment(val farmer: Farmer) : Fragment(), SensorsPagingAdapter.SensorClickEvent {
    private var _binding: FragmentFarmerSensorsBinding? = null
    private val binding get() = checkNotNull(_binding)
    private val adminViewModel by viewModel<AdminViewModel>()
    private val allSensorsAdapter = SensorsPagingAdapter(this)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFarmerSensorsBinding.inflate(inflater, container, false)

        initViews()
        initObservers()
        initListeners()


        parentFragmentManager.addOnBackStackChangedListener {
            if (isVisible){
                initObservers()
                allSensorsAdapter.refresh()
            }
        }

        return binding.root
    }

    private fun initObservers() {

        if (requireContext().isOnline()){
            adminViewModel.getAllSensorsByFarmerId(farmer.id).onEach{
                allSensorsAdapter.submitData(it)
            }.launchIn(lifecycleScope)
        } else {
            showToast(getString(R.string.check_network_connection))
        }



        lifecycleScope.launch {
            allSensorsAdapter.loadStateFlow.collect{
                val state = it.refresh
                binding.swipeRefreshAllSensors.isRefreshing = state is LoadState.Loading
            }
        }
    }

    private fun initListeners() {


        binding.fabCreateNewSensor.setOnClickListener {
            addFragment(parentFragmentManager, R.id.fragment_parent_container, AttachSensorFragment(farmer))
        }

    }

    private fun initViews(){
        binding.swipeRefreshAllSensors.setColorSchemeColors(resources.getColor(R.color.colorPrimary))
        binding.recyclerViewAllSensors.adapter = allSensorsAdapter
    }




    override fun onDestroyView() {
        _binding =null
        super.onDestroyView()
    }

    override fun onSensorClick(sensor: Sensor) {
        //
    }
}