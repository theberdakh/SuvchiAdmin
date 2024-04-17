package com.theberdakh.suvchiadmin.ui.farmers

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.theberdakh.suvchiadmin.R
import com.theberdakh.suvchiadmin.data.remote.farmers.models.Farmer
import com.theberdakh.suvchiadmin.data.remote.regions.models.Region
import com.theberdakh.suvchiadmin.databinding.FragmentAllFarmersBinding
import com.theberdakh.suvchiadmin.presentation.AdminViewModel
import com.theberdakh.suvchiadmin.ui.add_farmer.AddFarmerFragment
import com.theberdakh.suvchiadmin.ui.farmer_profile.FarmerProfileFragment
import com.theberdakh.suvchiadmin.ui.farmers.adapter.FarmersAdapter
import com.theberdakh.suvchiadmin.utils.addFragment
import com.theberdakh.suvchiadmin.utils.setUpBackNavigation
import com.theberdakh.suvchiadmin.utils.showToast
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

const val TAG = "AllFarmersFragment"

class AllFarmersFragment(private val region: Region) : Fragment(), FarmersAdapter.FarmerClickEvent {
    private var _binding: FragmentAllFarmersBinding? = null
    private val binding get() = checkNotNull(_binding)
    private val adminViewModel by viewModel<AdminViewModel>()
    private val farmersAdapter = FarmersAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllFarmersBinding.inflate(inflater, container, false)

        Log.d(TAG, "Region is ${region.name}")
        initViews()
        initListeners()
        initObservers()

        refreshDataOnBack()



        return binding.root
    }

    private fun refreshDataOnBack() {
        parentFragmentManager.addOnBackStackChangedListener {
            if (isVisible) {
                initObservers()
                farmersAdapter.refresh()
            }
        }
    }

    private fun initObservers() {

        lifecycleScope.launch {
            adminViewModel.getAllFarmersByRegionId(regionId = region.id).collect {
                farmersAdapter.submitData(it)

            }
        }

        lifecycleScope.launch {
            farmersAdapter.loadStateFlow.collect {
                val state = it.refresh
                binding.swipeRefreshAllFarmers.isRefreshing = state is LoadState.Loading
            }
        }

    }

    private fun initViews() {
        binding.toolbarAllFarmers.title = region.name
        binding.recyclerViewAllFarmers.adapter = farmersAdapter
        binding.swipeRefreshAllFarmers.setColorSchemeColors(resources.getColor(R.color.colorPrimary))
    }

    private fun initListeners() {

        binding.swipeRefreshAllFarmers.setOnRefreshListener {
            initObservers()
        }
        binding.toolbarAllFarmers.setUpBackNavigation(parentFragmentManager)
        binding.fabCreateNewFarmer.setOnClickListener {
            addFragment(
                parentFragmentManager, R.id.fragment_parent_container,
                AddFarmerFragment()
            )
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onClick(farmer: Farmer) {
        addFragment(
            parentFragmentManager,
            R.id.fragment_parent_container,
            FarmerProfileFragment(farmer)
        )
    }


}