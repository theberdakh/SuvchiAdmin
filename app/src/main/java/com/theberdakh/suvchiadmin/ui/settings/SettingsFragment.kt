package com.theberdakh.suvchiadmin.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.theberdakh.suvchiadmin.R
import com.theberdakh.suvchiadmin.data.local.SharedPreferences
import com.theberdakh.suvchiadmin.databinding.FragmentSettingsBinding

class SettingsFragment: Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = checkNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        initViews()
        initListeners()


        return binding.root
    }

    private fun initListeners() {
        binding.textViewLogOut.setOnClickListener {
            showWarningDialog()
        }
    }

    private fun logOut() {
        SharedPreferences.clearUserData()
        navigateBackToLoginFragment()
    }

    private fun showWarningDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.shape_corner))
            .setTitle(R.string.log_out)
            .setMessage(getString(R.string.log_out_warning))
            .setPositiveButton(getString(R.string.close)){ dialog, which ->
                logOut()
            }
            .setNegativeButton(getString(R.string.dismiss)){ dialog, which ->
                dialog.dismiss()
            }.show()

    }

    private fun navigateBackToLoginFragment() {
        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.fragment_parent_container) as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.login_nav)
        navHostFragment.navController.graph = graph
    }

    private fun initViews() {
        SharedPreferences().apply {
            binding.textviewFullName.text = getString(R.string.full_name, firstName, lastName)
            binding.textviewPhone.text = getString(R.string.phone_number, phone)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}