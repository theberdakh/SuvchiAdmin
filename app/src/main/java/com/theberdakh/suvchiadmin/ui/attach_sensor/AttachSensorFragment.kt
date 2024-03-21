package com.theberdakh.suvchiadmin.ui.attach_sensor

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
import com.theberdakh.suvchiadmin.databinding.FragmentAttachSensorBinding
import com.theberdakh.suvchiadmin.presentation.AdminViewModel
import com.theberdakh.suvchiadmin.ui.all_sensors.SensorsPagingAdapter
import com.theberdakh.suvchiadmin.utils.showToast
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AttachSensorFragment(val farmer: Farmer): Fragment(), SensorsPagingAdapter.SensorClickEvent {
    private var _binding: FragmentAttachSensorBinding? = null
    private val binding get() = checkNotNull(_binding)
    private val allSensorsAdapter = SensorsPagingAdapter(this)
    private val adminViewModel by viewModel<AdminViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAttachSensorBinding.inflate(inflater, container, false)


        initViews()
        initListeners()
        initObservers()
        return binding.root
    }

    private fun initListeners() {
        binding.toolbarAttachSensor.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        binding.swipeRefreshAttachSensors.setOnRefreshListener {
            initObservers()
        }
    }

    private fun initObservers() {
        adminViewModel.attachSensorSuccess.onEach {
            showToast(it.message)
            requireActivity().supportFragmentManager.popBackStack()
        }.launchIn(lifecycleScope)

        adminViewModel.attachSensorMessage.onEach {
            showToast(it)
        }.launchIn(lifecycleScope)

        adminViewModel.attachSensorError.onEach {
            it.printStackTrace()
        }.launchIn(lifecycleScope)

        lifecycleScope.launch {
            adminViewModel.sensors.collect {
                allSensorsAdapter.submitData(it)
            }
        }

        lifecycleScope.launch {
            allSensorsAdapter.loadStateFlow.collect{
                val state = it.refresh
                binding.swipeRefreshAttachSensors.isRefreshing = state is LoadState.Loading
            }
        }

    }

    private fun initViews() {
        binding.swipeRefreshAttachSensors.setColorSchemeColors(resources.getColor(R.color.colorPrimary))
        binding.recyclerAllSensorsToAttach.adapter = allSensorsAdapter
        binding.toolbarAttachSensor.subtitle = getString(R.string.helper_attach_sensor_to_farmer)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onSensorClick(sensor: Sensor) {
        lifecycleScope.launch {
            adminViewModel.attachSensor(sensorId = sensor.id, farmerId =  farmer.id)
        }
    }
}