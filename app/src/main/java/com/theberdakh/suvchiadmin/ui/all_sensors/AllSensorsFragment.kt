package com.theberdakh.suvchiadmin.ui.all_sensors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.theberdakh.suvchiadmin.R
import com.theberdakh.suvchiadmin.data.remote.sensors.models.Sensor
import com.theberdakh.suvchiadmin.databinding.FragmentAllSensorsBinding
import com.theberdakh.suvchiadmin.presentation.AdminViewModel
import com.theberdakh.suvchiadmin.utils.showToast
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AllSensorsFragment : Fragment(), SensorsPagingAdapter.SensorClickEvent {
    private var _binding: FragmentAllSensorsBinding? = null
    private val binding get() = checkNotNull(_binding)
    private val allSensorsAdapter = SensorsPagingAdapter(this)
    private val adminViewModel by viewModel<AdminViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllSensorsBinding.inflate(inflater, container, false)

        initViews()
        initObservers()
        initListeners()


        return binding.root
    }

    private fun initListeners() {
        binding.swipeRefreshAllSensors.setOnRefreshListener {
            initObservers()
        }
    }

    private fun initObservers() {
        lifecycleScope.launch {
            adminViewModel.sensors.collect {
                allSensorsAdapter.submitData(it)
            }
        }

        lifecycleScope.launch {
            allSensorsAdapter.loadStateFlow.collect{
                val state = it.refresh
                binding.swipeRefreshAllSensors.isRefreshing = state is LoadState.Loading
            }
        }

    }

    private fun initViews() {
        binding.swipeRefreshAllSensors.setColorSchemeColors(resources.getColor(R.color.colorPrimary))
        binding.recyclerAllSensors.adapter = allSensorsAdapter
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onSensorClick(sensor: Sensor) {
        showToast(sensor.name)
    }
}