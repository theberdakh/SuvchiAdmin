package com.theberdakh.suvchiadmin.ui.farmer_contracts

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
import com.theberdakh.suvchiadmin.data.remote.contract.models.ContractByFarmerId
import com.theberdakh.suvchiadmin.data.remote.farmers.models.Farmer
import com.theberdakh.suvchiadmin.databinding.FragmentContractsBinding
import com.theberdakh.suvchiadmin.presentation.AdminViewModel
import com.theberdakh.suvchiadmin.ui.add_contract.AddContractFragment
import com.theberdakh.suvchiadmin.ui.contracts.ContractsPagingAdapter
import com.theberdakh.suvchiadmin.utils.addFragment
import com.theberdakh.suvchiadmin.utils.downloadFile
import com.theberdakh.suvchiadmin.utils.showToast
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FarmerContractsFragment(val farmer: Farmer) : Fragment(),
    ContractsByFarmerIdPagingAdapter.ContractByFarmerIdClickEvent {
    private var _binding: FragmentContractsBinding? = null
    private val binding get() = checkNotNull(_binding)
    private val adminViewModel by viewModel<AdminViewModel>()
    private val contractsByFarmerIdPagingAdapter = ContractsByFarmerIdPagingAdapter(this)

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

    private fun initObservers() {

        adminViewModel.getAllContractsByFarmerId(farmer.id).onEach {
            contractsByFarmerIdPagingAdapter.submitData(it)
        }.launchIn(lifecycleScope)
        lifecycleScope.launch {

        }

        lifecycleScope.launch {
            contractsByFarmerIdPagingAdapter.loadStateFlow.collect {
                val state = it.refresh
                binding.swipeRefreshAllContracts.isRefreshing = state is LoadState.Loading
            }
        }
    }

    private fun initListeners() {

        binding.fabCreateNewContract.setOnClickListener {
            addFragment(
                parentFragmentManager,
                R.id.fragment_parent_container,
                AddContractFragment(farmer)
            )
        }

        binding.swipeRefreshAllContracts.setOnRefreshListener {
            initObservers()
        }

    }

    private fun initViews() {
        binding.recyclerViewContracts.adapter = contractsByFarmerIdPagingAdapter
        binding.swipeRefreshAllContracts.setColorSchemeColors(resources.getColor(R.color.colorPrimary))
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onClick(contract: ContractByFarmerId) {

        val contractPng = "https://smartwaterdegree.uz/storage/f9c2a658ba8781789f32282282de7bdd.png"
        val contractPdf = "https://smartwaterdegree.uz/storage/9978a8fbbaad97f87a8562d80ff47905.pdf"
        requireActivity().downloadFile(contractPng, "contract.${contractPng.substring(contractPng.length - 3, contractPng.length)}")
    }

}