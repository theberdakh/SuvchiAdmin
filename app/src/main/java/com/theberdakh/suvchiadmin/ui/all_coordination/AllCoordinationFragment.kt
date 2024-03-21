package com.theberdakh.suvchiadmin.ui.all_coordination

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.theberdakh.suvchiadmin.R
import com.theberdakh.suvchiadmin.databinding.FragmentAllCoordinationsBinding
import com.theberdakh.suvchiadmin.presentation.AdminViewModel
import com.theberdakh.suvchiadmin.ui.add_coordination.AddCoordinationFragment
import com.theberdakh.suvchiadmin.ui.all_coordination.paging.CoordinationAdapter
import com.theberdakh.suvchiadmin.utils.addFragment
import com.theberdakh.suvchiadmin.utils.addFragmentToBackStack
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
            addFragment(requireActivity().supportFragmentManager, R.id.fragment_parent_container, AddCoordinationFragment())
        }
    }

    override fun onResume() {
        Log.d("AllCordination", "OnResume")
        super.onResume()
    }

    override fun onStart() {
        Log.d("AllCordination", "OnStart")
        super.onStart()
    }


    override fun onAttach(context: Context) {
        Log.d("AllCordination", "OnAttach")
        super.onAttach(context)
    }


    private fun initObservers() {
        lifecycleScope.launch {
            adminViewModel.coordination.collect {
                coordinationAdapter.submitData(it)
            }
        }

        lifecycleScope.launch{
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
    }
}