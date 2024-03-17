package com.theberdakh.suvchiadmin.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.theberdakh.suvchiadmin.R
import com.theberdakh.suvchiadmin.data.remote.regions.models.Region
import com.theberdakh.suvchiadmin.databinding.FragmentDashboardBinding
import com.theberdakh.suvchiadmin.presentation.AdminViewModel
import com.theberdakh.suvchiadmin.ui.dashboard.adapter.RegionsAdapter
import com.theberdakh.suvchiadmin.ui.farmers.AllFarmersFragment
import com.theberdakh.suvchiadmin.utils.addFragmentToBackStack
import com.theberdakh.suvchiadmin.utils.replaceFragment
import com.theberdakh.suvchiadmin.utils.showToast
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardFragment: Fragment(), RegionsAdapter.RegionClickEvent {
    private var _binding: FragmentDashboardBinding? =null
    private val binding get() = checkNotNull(_binding)
    private val adminViewModel by viewModel<AdminViewModel>()
    private val regionsAdapter = RegionsAdapter(this)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)


        initObservers()
        initViews()
        initListeners()


        return binding.root
    }


    private fun initViews(){
        initRecyclerView()
        binding.swipeRefreshDashboard.setColorSchemeColors(resources.getColor(R.color.colorPrimary))

    }

    private fun initRecyclerView() {
        binding.recyclerDashboard.adapter =regionsAdapter
    }

    private fun  initListeners(){
        binding.swipeRefreshDashboard.setOnRefreshListener {
            initObservers()
        }
    }
    private fun initObservers() {

        lifecycleScope.launch {
            adminViewModel.regions.collect {
                regionsAdapter.submitData(it)
            }
        }

        lifecycleScope.launch {
            regionsAdapter.loadStateFlow.collect{
                val state = it.refresh
                binding.swipeRefreshDashboard.isRefreshing = state is LoadState.Loading
            }
        }

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onClick(region: Region) {
        addFragmentToBackStack(requireActivity().supportFragmentManager, R.id.fragment_parent_container, AllFarmersFragment(region))
    }


}