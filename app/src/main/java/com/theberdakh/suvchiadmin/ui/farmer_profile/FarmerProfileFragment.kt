package com.theberdakh.suvchiadmin.ui.farmer_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import coil.load
import com.google.android.material.tabs.TabLayoutMediator
import com.theberdakh.suvchiadmin.R
import com.theberdakh.suvchiadmin.data.remote.contract.models.Contract
import com.theberdakh.suvchiadmin.data.remote.farmers.models.Farmer
import com.theberdakh.suvchiadmin.databinding.FragmentFarmerProfileBinding
import com.theberdakh.suvchiadmin.presentation.AdminViewModel
import com.theberdakh.suvchiadmin.ui.add_farmer.AddFarmerFragment
import com.theberdakh.suvchiadmin.ui.contracts.ContractsFragment
import com.theberdakh.suvchiadmin.ui.contracts.ContractsPagingAdapter
import com.theberdakh.suvchiadmin.ui.dashboard.DashboardFragment
import com.theberdakh.suvchiadmin.ui.farmer_profile.info.FarmerProfileInfoFragment
import com.theberdakh.suvchiadmin.ui.farmer_profile.sensors.FarmerSensorsFragment
import com.theberdakh.suvchiadmin.ui.sensors.AllSensorsFragment
import com.theberdakh.suvchiadmin.utils.addFragment
import com.theberdakh.suvchiadmin.utils.addFragmentToBackStack
import com.theberdakh.suvchiadmin.utils.showToast
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FarmerProfileFragment(val farmer: Farmer) : Fragment(), ContractsPagingAdapter.ContractClickEvent {
    private var _binding: FragmentFarmerProfileBinding? = null
    private val binding get() = checkNotNull(_binding)
    private val adminViewModel by viewModel<AdminViewModel>()
    private val contractsAdapter = ContractsPagingAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFarmerProfileBinding.inflate(inflater, container, false)


        initViews()
        initListeners()
        initObservers()

        return binding.root
    }

    private fun initObservers(){

        lifecycleScope.launch {
            adminViewModel.contracts.collect {
                contractsAdapter.submitData(it)
            }
        }

        lifecycleScope.launch {
            contractsAdapter.loadStateFlow.collect{
                val state = it.refresh
             /*   binding.swipeRefreshDashboard.isRefreshing = state is LoadState.Loading*/
            }
        }
    }

    private fun initListeners() {
        binding.toolbarAllFarmers.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
        binding.toolbarAllFarmers.setOnMenuItemClickListener {menuItem ->
            if(menuItem.itemId == R.id.action_menu_add_sensor){
                showToast("clicked")
                parentFragment?.let {
                    addFragment(
                        parentFragmentManager, R.id.fragment_parent_container,
                        AllSensorsFragment()
                    )
                }
            }
            true
        }
    }

    private fun initViews() {
        val viewPagerAdapter = FarmerProfileViewPager(listOf(DashboardFragment(), ContractsFragment(),  AllSensorsFragment()), parentFragmentManager, requireActivity().lifecycle)
        binding.viewPager.adapter = viewPagerAdapter
        binding.toolbarAllFarmers.title = resources.getString(R.string.full_name, farmer.firstName, farmer.lastName)

        TabLayoutMediator(binding.tabLayout, binding.viewPager){ tab, position ->
            when(position){
                0 -> {
                    tab.text = "Dashboard"
                }
                1 -> {
                    tab.text = "Contracts"
                }
                2 -> {
                    tab.text = "All sensors"
                }
            }
        }.attach()


    }

    override fun onDestroyView() {
        _binding = null


        super.onDestroyView()
    }

    override fun onClick(contract: Contract) {

    }
}