package com.theberdakh.suvchiadmin.ui.add_coordination

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import com.theberdakh.suvchiadmin.R
import com.theberdakh.suvchiadmin.databinding.FragmentAddCoordinationBinding
import com.theberdakh.suvchiadmin.presentation.AdminViewModel
import com.theberdakh.suvchiadmin.utils.getString
import com.theberdakh.suvchiadmin.utils.isEmptyOrBlank
import com.theberdakh.suvchiadmin.utils.shakeIf
import com.theberdakh.suvchiadmin.utils.showToast
import com.theberdakh.suvchiadmin.utils.vibratePhone
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddCoordinationFragment: Fragment() {
    private var _binding: FragmentAddCoordinationBinding? = null
    private val binding get() = checkNotNull(_binding)
    private val adminViewModel by viewModel<AdminViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddCoordinationBinding.inflate(inflater, container, false)
        initListeners()
        initObservers()

        return binding.root
    }

    private fun initObservers() {
        adminViewModel.responseCreateCoordinationSuccess.onEach {
            requireActivity().supportFragmentManager.popBackStack()
        }.launchIn(lifecycleScope)

        adminViewModel.responseCreateCoordinationMessage.onEach {
            showToast(it)
        }.launchIn(lifecycleScope)

        adminViewModel.responseCreateCoordinationError.onEach {
            it.printStackTrace()
        }.launchIn(lifecycleScope)
    }

    private fun initListeners() {
        binding.toolbarAddCoordination.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }




        binding.toolbarAddCoordination.setOnMenuItemClickListener{menuItem ->
            val hIsValid = binding.editTextAddH.shakeIf { it.isEmptyOrBlank() }
            val qIsValid = binding.editTextAddQ.shakeIf { it.isEmptyOrBlank() }

            Log.d("h", "$hIsValid")
            Log.d("q", "$qIsValid")

            if (hIsValid && qIsValid){
                if (menuItem.itemId == R.id.action_menu_add_coordination){
                    val h = Integer.parseInt(binding.editTextAddH.getString())
                    val q = Integer.parseInt(binding.editTextAddQ.getString())
                    lifecycleScope.launch {
                        adminViewModel.createCoordination(h = h, q = q)
                    }
                }
            } else {
                requireActivity().vibratePhone()
            }


            true
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}