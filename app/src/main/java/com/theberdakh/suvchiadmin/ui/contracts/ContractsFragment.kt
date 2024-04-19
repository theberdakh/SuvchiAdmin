package com.theberdakh.suvchiadmin.ui.contracts

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.theberdakh.suvchiadmin.R
import com.theberdakh.suvchiadmin.data.remote.contract.models.Contract
import com.theberdakh.suvchiadmin.data.remote.utils.isOnline
import com.theberdakh.suvchiadmin.databinding.FragmentContractsBinding
import com.theberdakh.suvchiadmin.presentation.AdminViewModel
import com.theberdakh.suvchiadmin.ui.add_contract.AddContractFragment
import com.theberdakh.suvchiadmin.ui.farmers.AllFarmersFragment
import com.theberdakh.suvchiadmin.utils.addFragmentToBackStack
import com.theberdakh.suvchiadmin.utils.downloadFile
import com.theberdakh.suvchiadmin.utils.invisible
import com.theberdakh.suvchiadmin.utils.showToast
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ContractsFragment: Fragment(), ContractsPagingAdapter.ContractClickEvent {
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

            if (requireContext().isOnline()){
                adminViewModel.contracts.collectLatest {
                    allContractsAdapter.submitData(it)
                }
            } else {
                showToast(getString(R.string.check_network_connection))
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

        binding.swipeRefreshAllContracts.setOnRefreshListener {
            initObservers()
        }

    }

    private fun initViews(){
        binding.fabCreateNewContract.invisible()
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