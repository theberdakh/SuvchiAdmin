package com.theberdakh.suvchiadmin.ui.farmer_info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.theberdakh.suvchiadmin.data.remote.farmers.models.Farmer
import com.theberdakh.suvchiadmin.databinding.FragmentFarmerInfoBinding

class FarmerProfileInfoFragment(val farmer: Farmer): Fragment() {
    private var _binding: FragmentFarmerInfoBinding? = null
    private val binding get() = checkNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFarmerInfoBinding.inflate(inflater, container, false)

        initViews()


        return binding.root
    }

    private fun initViews() {
        binding.textInputEditTextName.setText(farmer.firstName)
        binding.textInputEditTextLastName.setText(farmer.lastName)
        binding.textInputEditTextMiddleName.setText(farmer.middleName)
        binding.textInputEditTextPassport.setText(farmer.passport)
        binding.textInputEditTextPhoneNumber.setText(farmer.phone)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}