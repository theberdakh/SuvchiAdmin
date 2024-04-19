package com.theberdakh.suvchiadmin.ui.all_coordination

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.theberdakh.suvchiadmin.R
import com.theberdakh.suvchiadmin.data.remote.utils.isOnline
import com.theberdakh.suvchiadmin.databinding.FragmentAllCoordinationsBinding
import com.theberdakh.suvchiadmin.presentation.AdminViewModel
import com.theberdakh.suvchiadmin.ui.add_coordination.AddCoordinationFragment
import com.theberdakh.suvchiadmin.ui.all_coordination.paging.CoordinationAdapter
import com.theberdakh.suvchiadmin.utils.addFragment
import com.theberdakh.suvchiadmin.utils.addFragmentToBackStack
import com.theberdakh.suvchiadmin.utils.showToast
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AllCoordinationFragment : Fragment() {
    private var _binding: FragmentAllCoordinationsBinding? = null
    private val binding get() = checkNotNull(_binding)
    private val adminViewModel by viewModel<AdminViewModel>()
    private val coordinationAdapter = CoordinationAdapter()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        Log.d("AllCordination", "OnCreate")
        _binding = FragmentAllCoordinationsBinding.inflate(inflater, container, false)

        parentFragmentManager.addOnBackStackChangedListener {
            if (isVisible) {
                initObservers()
                coordinationAdapter.refresh()
            }
        }

        initViews()
        initObservers()
        initListeners()

        return binding.root
    }


    private fun initListeners() {
        binding.toolbarAllCoordination.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        binding.swipeRefreshAllCoordination.setOnRefreshListener {
            initObservers()
            coordinationAdapter.refresh()
        }
        binding.fabCreateNewCoordination.setOnClickListener {
            addFragment(
                requireActivity().supportFragmentManager,
                R.id.fragment_parent_container,
                AddCoordinationFragment()
            )
        }
    }


    private fun initObservers() {
        lifecycleScope.launch {
            if (requireContext().isOnline()){
                adminViewModel.coordination.collect {
                    coordinationAdapter.submitData(it)
                }

            } else {
                showToast(getString(R.string.check_network_connection))
            }
        }

        lifecycleScope.launch {
            coordinationAdapter.loadStateFlow.collect {
                val state = it.refresh
                binding.swipeRefreshAllCoordination.isRefreshing = state is LoadState.Loading
            }
        }


    }

    private fun initViews() {
        binding.recyclerViewAllCoordination.adapter = coordinationAdapter
        binding.swipeRefreshAllCoordination.setColorScheme(R.color.colorPrimary)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
        parentFragmentManager.removeOnBackStackChangedListener {
            showToast("visible")

        }
    }
}