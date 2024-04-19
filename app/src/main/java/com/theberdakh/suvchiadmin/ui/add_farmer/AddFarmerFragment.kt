package com.theberdakh.suvchiadmin.ui.add_farmer

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.RoundedCornersTransformation
import com.theberdakh.suvchiadmin.R
import com.theberdakh.suvchiadmin.data.remote.farmers.models.CreateFarmerRequestBody
import com.theberdakh.suvchiadmin.data.remote.regions.models.Region
import com.theberdakh.suvchiadmin.data.remote.utils.isOnline
import com.theberdakh.suvchiadmin.databinding.FragmentAddFarmerBinding
import com.theberdakh.suvchiadmin.presentation.AdminViewModel
import com.theberdakh.suvchiadmin.utils.getString
import com.theberdakh.suvchiadmin.utils.isEmptyOrBlank
import com.theberdakh.suvchiadmin.utils.shakeIf
import com.theberdakh.suvchiadmin.utils.shakeIfEmptyOrBlank
import com.theberdakh.suvchiadmin.utils.showSnackbar
import com.theberdakh.suvchiadmin.utils.showToast
import com.theberdakh.suvchiadmin.utils.vibratePhone
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddFarmerFragment : Fragment() {
    private var _binding: FragmentAddFarmerBinding? = null
    private val binding get() = checkNotNull(_binding)
    private val adminViewModel by viewModel<AdminViewModel>()
    private var isPhotoSelected = false
    private val regionsPair = mutableListOf<Pair<String, Int>>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddFarmerBinding.inflate(inflater, container, false)

        initViews()
        initListeners()
        initObservers()

        return binding.root
    }

    private fun initObservers() {
        lifecycleScope.launch {
            adminViewModel.getAllRegionsByStateId(stateId = 1)
        }

        adminViewModel.responseAllRegionsByStateIdSuccess.onEach { regionDataResponse ->
            val listOfRegions = getRegionsFromPair(regionDataResponse.data)
            val regionsAdapter =
                ArrayAdapter(requireContext(), R.layout.item_list_dropdown, listOfRegions)
            binding.autoCompleteDropDownAddRegion.setAdapter(regionsAdapter)

        }.launchIn(lifecycleScope)

        adminViewModel.responseCreateFarmerSuccess.onEach {
            binding.toolbarAllFarmers.showSnackbar(
                getString(
                    R.string.farmer_created,
                    it.firstName,
                    it.lastName
                ))
            parentFragmentManager.popBackStack()
        }.launchIn(lifecycleScope)

        adminViewModel.responseCreateFarmerMessage.onEach {
            showToast(it)
        }.launchIn(lifecycleScope)

    }

    private fun getRegionsFromPair(regions: List<Region>): List<String> {
        for (region in regions) {
            regionsPair.add(Pair(region.name, region.id))
        }
        return regionsPair.map { it.first }
    }

    private fun getSelectedRegionId(text: String): Int {
        val pair = regionsPair.find {
            it.first == text
        }
        return if (pair != null) {
            Log.d("Selected Region", "${pair.first}, ${pair.second}")
            pair.second
        } else {
            -1
        }
    }


    private fun initViews() {
        initGendersDropDown()
    }

    private fun initGendersDropDown() {
        val array = arrayOf(getString(R.string.male), getString(R.string.female))
        val gendersAdapter = ArrayAdapter(requireContext(), R.layout.item_list_dropdown, array)
        binding.autoCompleteDropDownAddGender.setAdapter(gendersAdapter)
    }

    private fun initListeners() {
        binding.layoutAddPhoto.setOnClickListener {
            isPhotoSelected = if (isPhotoSelected) {
                removePhoto()
                false
            } else {
                selectPhoto()
                true
            }
        }
        binding.toolbarAllFarmers.setOnMenuItemClickListener { menuItem ->

            if (menuItem.itemId == R.id.action_menu_add_farmer) {
                if (fieldsAreValid()) {
                   val farmer =  createFarmer()
                    postFarmer(farmer)
                } else {
                    requireActivity().vibratePhone()
                    binding.toolbarAllFarmers.showSnackbar(getString(R.string.fill_all_blanks))
                }

            }
            true
        }
    }

    private fun postFarmer(farmer: CreateFarmerRequestBody) {
        lifecycleScope.launch {
            if (requireContext().isOnline()){
                adminViewModel.createFarmer(farmer)
            } else {
                showToast(getString(R.string.check_network_connection))
            }

        }
    }

    private fun createFarmer(): CreateFarmerRequestBody {

        val phone = "998${binding.editTextAddPhoneNumber.getString()}"
        val gender =
            if (binding.autoCompleteDropDownAddGender.getString() == getString(R.string.male)) "male" else "fale"
        val regionId = getSelectedRegionId(binding.autoCompleteDropDownAddRegion.getString())
        val longitude = "59.605049"
        val latitude = "42.460411"
        val passport = "AD0166520"

        Log.d("Phone", phone)

        return CreateFarmerRequestBody(
            phone = phone,
            latitude = latitude,
            longitude = longitude,
            gender = gender,
            regionId = regionId,
            middleName = binding.editTextAddMiddleName.getString(),
            firstName = binding.editTextAddName.getString(),
            lastName = binding.editTextAddLastName.getString(),
            password = binding.editTextAddPassword.getString(),
            username = binding.editTextAddUsername.getString(),
            passport = binding.editTextAddPassportSeries.getString(),
            K = Integer.parseInt(binding.editTextAddK.getString()))
    }

    private fun fieldsAreValid(): Boolean {

        val nameIsValid = binding.editTextAddName.shakeIfEmptyOrBlank()
        val lastNameIsValid = binding.editTextAddLastName.shakeIfEmptyOrBlank()
        val middleNameIsValid = binding.editTextAddMiddleName.shakeIfEmptyOrBlank()
        val regionIsValid = binding.autoCompleteDropDownAddRegion.shakeIf { regionName ->
            getSelectedRegionId(regionName) == -1
        }
        val genderIsValid = binding.autoCompleteDropDownAddGender.shakeIfEmptyOrBlank()
        val passportSeriesIsValid = binding.editTextAddPassportSeries.shakeIfEmptyOrBlank()
        val passportPhoneNumberIsValid =
            binding.editTextAddPhoneNumber.shakeIf { phoneNumber -> phoneNumber.length != 9 }
        val usernameIsValid = binding.editTextAddUsername.shakeIfEmptyOrBlank()
        val passwordIsValid = binding.editTextAddPassword.shakeIf {
            if(it.length < 10) showToast("Belgiler sanı 10 nan kem bolmawı kerek")
            it.isEmptyOrBlank() || it.length <10
        }
        val kIsValid = binding.editTextAddK.shakeIf { K ->
            val parsedInt = K.toIntOrNull()
            parsedInt==null
        }


        return nameIsValid && lastNameIsValid && middleNameIsValid && regionIsValid && genderIsValid && passportSeriesIsValid && passportPhoneNumberIsValid &&
                usernameIsValid && passwordIsValid && kIsValid


    }

    private fun removePhoto() {
        binding.imageviewAddPhotoIcon.load(R.drawable.round_add_photo_24)
        binding.textviewCaptionAddPhoto.text = getString(R.string.add_profile_pic)
    }

    private fun selectPhoto() {
        launcher.launch(
            PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly)
                .build()
        )

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private var launcher = registerForActivityResult<PickVisualMediaRequest, Uri>(
        ActivityResultContracts.PickVisualMedia()
    ) { result ->
        if (result == null) {
            binding.imageviewAddPhotoIcon.showSnackbar(getString(R.string.no_photo_selected))
        } else {
            binding.textviewCaptionAddPhoto.text = getString(R.string.remove_profile_photo)
            binding.imageviewAddPhotoIcon.load(result) {
                crossfade(true)
                transformations(RoundedCornersTransformation(13f, 13f, 13f, 13f))
            }
        }
    }
}