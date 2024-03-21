package com.theberdakh.suvchiadmin.ui.farmer_contracts

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.theberdakh.suvchiadmin.R
import com.theberdakh.suvchiadmin.data.remote.contract.models.Contract
import com.theberdakh.suvchiadmin.data.remote.farmers.models.Farmer
import com.theberdakh.suvchiadmin.databinding.FragmentContractsBinding
import com.theberdakh.suvchiadmin.presentation.AdminViewModel
import com.theberdakh.suvchiadmin.ui.add_contract.AddContractFragment
import com.theberdakh.suvchiadmin.ui.contracts.ContractsPagingAdapter
import com.theberdakh.suvchiadmin.utils.addFragment
import com.theberdakh.suvchiadmin.utils.addFragmentToBackStack
import com.theberdakh.suvchiadmin.utils.downloadFile
import com.theberdakh.suvchiadmin.utils.invisible
import com.theberdakh.suvchiadmin.utils.showToast
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FarmerContractsFragment(val farmer: Farmer): Fragment() , ContractsPagingAdapter.ContractClickEvent {
    private var _binding: FragmentContractsBinding? = null
    private val binding get() = checkNotNull(_binding)
    private val adminViewModel by viewModel<AdminViewModel>()
    private val allContractsAdapter = ContractsPagingAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContractsBinding.inflate(inflater, container, false)

        initObservers()
        initViews()
        initListeners()

        return binding.root
    }

    private fun initObservers(){
        lifecycleScope.launch {
            adminViewModel.contracts.collectLatest {
                allContractsAdapter.submitData(it)
            }
        }

        lifecycleScope.launch {
            allContractsAdapter.loadStateFlow.collect{
                val state = it.refresh
                binding.swipeRefreshAllContracts.isRefreshing = state is LoadState.Loading
            }
        }
    }

    private fun initListeners() {

        binding.fabCreateNewContract.setOnClickListener {


        addFragment(parentFragmentManager, R.id.fragment_parent_container, AddContractFragment(farmer))
        }

        binding.swipeRefreshAllContracts.setOnRefreshListener {
            initObservers()
        }

    }

    object CustomScrollChangeListener:  RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            when(newState){
                RecyclerView.SCROLL_STATE_IDLE -> Log.d("Recycler", "idle")
                RecyclerView.SCROLL_STATE_DRAGGING -> Log.d("Recycler", "dragging")
                RecyclerView.SCROLL_STATE_SETTLING -> Log.d("Recycler", "settling")
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (dy > 0) {
                showToast("Scrolled Downwards");
            } else if (dy < 0) {
                showToast("Scrolled Upwards");
            } else {
                showToast("No Vertical Scrolled");
            }
            super.onScrolled(recyclerView, dx, dy)
        }
    }

    private fun initViews(){
        binding.recyclerViewContracts.adapter = allContractsAdapter
        binding.swipeRefreshAllContracts.setColorSchemeColors(resources.getColor(R.color.colorPrimary))
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onClick(contract: Contract) {
        requireActivity().downloadFile(contract.file, contract.title)
    }
}