package com.theberdakh.suvchiadmin.ui.add_sensor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.theberdakh.suvchiadmin.R
import com.theberdakh.suvchiadmin.data.remote.farmers.models.Farmer
import com.theberdakh.suvchiadmin.databinding.FragmentAddSensorBinding
import com.theberdakh.suvchiadmin.presentation.AdminViewModel
import com.theberdakh.suvchiadmin.utils.getString
import com.theberdakh.suvchiadmin.utils.isEmptyOrBlank
import com.theberdakh.suvchiadmin.utils.shake
import com.theberdakh.suvchiadmin.utils.shakeIf
import com.theberdakh.suvchiadmin.utils.showToast
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddSensorFragment : Fragment() {
    private var _binding: FragmentAddSensorBinding? = null
    private val binding get() = checkNotNull(_binding)
    private val adminViewModel by viewModel<AdminViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddSensorBinding.inflate(inflater, container, false)

        initListeners()
        initObservers()


        return binding.root
    }

    private fun initObservers() {
        adminViewModel.responseCreateSensorSuccess.onEach {
            showToast("Created ${it.createdAt}")
            requireActivity().supportFragmentManager.popBackStack()
        }.launchIn(lifecycleScope)

        adminViewModel.responseCreateSensorMessage.onEach {
            showToast(it)
        }.launchIn(lifecycleScope)

        adminViewModel.responseCreateSensorError.onEach {
            it.printStackTrace()
        }.launchIn(lifecycleScope)
    }

    private fun initListeners() {
        binding.toolbarAddSensor.setOnMenuItemClickListener {
        if (it.itemId == R.id.action_menu_add_sensor){
            val nameIsValid = binding.editTextAddName.shakeIf { name -> name.isEmptyOrBlank() }
            val imeiIsValid =
                binding.editTextAddImei.shakeIf { imei -> imei.isEmptyOrBlank() || imei.length < 15 }
            if (nameIsValid && imeiIsValid) {
                lifecycleScope.launch {
                    adminViewModel.createSensor(binding.editTextAddName.getString(), binding.editTextAddImei.getString())
                }
            }
        }
            true
        }

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}