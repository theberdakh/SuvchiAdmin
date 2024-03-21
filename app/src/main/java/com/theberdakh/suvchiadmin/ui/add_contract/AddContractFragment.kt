package com.theberdakh.suvchiadmin.ui.add_contract

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.theberdakh.suvchiadmin.R
import com.theberdakh.suvchiadmin.data.remote.farmers.models.Farmer
import com.theberdakh.suvchiadmin.databinding.FragmentAddContractBinding
import com.theberdakh.suvchiadmin.presentation.AdminViewModel
import com.theberdakh.suvchiadmin.utils.getFileName
import com.theberdakh.suvchiadmin.utils.getFileType
import com.theberdakh.suvchiadmin.utils.getString
import com.theberdakh.suvchiadmin.utils.setUpBackNavigation
import com.theberdakh.suvchiadmin.utils.shakeIfEmptyOrBlank
import com.theberdakh.suvchiadmin.utils.showToast
import com.theberdakh.suvchiadmin.utils.vibratePhone
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class AddContractFragment(val farmer: Farmer): Fragment() {
    private var _binding: FragmentAddContractBinding? = null
    private val binding get() = checkNotNull(_binding)
    private val adminViewModel by viewModel<AdminViewModel>()
    private var fileId = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddContractBinding.inflate(inflater, container, false)

        initViews()
        initListeners()
        initObservers()

        return binding.root
    }

    private fun initObservers() {

        adminViewModel.responseCreateContractSuccess.onEach {
            showToast(getString(R.string.contract_successfully_created, it.title))
            parentFragmentManager.popBackStack()
        }.launchIn(lifecycleScope)
        adminViewModel.responseCreateContractMessage.onEach {
            showToast(it)
        }.launchIn(lifecycleScope)

        adminViewModel.responseCreateContractError.onEach {
            it.printStackTrace()
        }.launchIn(lifecycleScope)

        adminViewModel.responseUploadFileSuccess.onEach {
            fileId = it.id
        }.launchIn(lifecycleScope)
        adminViewModel.responseUploadFileMessage.onEach {
            showToast(it)
        }.launchIn(lifecycleScope)
        adminViewModel.responseUploadFileError.onEach {
            it.printStackTrace()
        }.launchIn(lifecycleScope)



    }

    private fun initListeners() {
        binding.layoutAddFile.setOnClickListener {
            contract.launch("*/*")
        }
        binding.toolbarCreateContract.setOnMenuItemClickListener {menuItem ->
            val contractTitleIsValid = binding.editTextAddTitle.shakeIfEmptyOrBlank()
            if (menuItem.itemId == R.id.action_menu_add_contract) {
                if (fileId != -1){
                    if (contractTitleIsValid) {
                        lifecycleScope.launch {
                            adminViewModel.createContract(binding.editTextAddTitle.getString(), fileId, farmer.id)
                        }
                    }
                } else {
                    requireActivity().vibratePhone()
                    showToast(getString(R.string.file_has_not_uploaded))
                }
            }
            true
        }
    }

    private fun initViews() {
        binding.toolbarCreateContract.setUpBackNavigation(parentFragmentManager)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private val contract = registerForActivityResult(ActivityResultContracts.GetContent()){
        binding.imageviewAddFileIcon.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.round_file_24))
        val fileName = requireNotNull(it).getFileName(requireActivity())
        val type = it.getFileType(requireActivity())
        binding.textviewCaptionAddFile.text = fileName

        if (fileName != null && type != null) {
            upload(fileName, it, type)
        }
    }

    private fun upload(fileName: String, fileUri: Uri, fileType: String) {
        val filesDir = requireActivity().filesDir
        val file = File(filesDir, fileName)
        val inputStream =  requireActivity().contentResolver.openInputStream(fileUri)
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)


        val requestBody = file.asRequestBody(fileType.toMediaType())
        val part = MultipartBody.Part.createFormData("file", file.name, requestBody)
        lifecycleScope.launch {
            adminViewModel.uploadContractFile(part)
            //Not uploading
        }
        inputStream?.close()
    }
}