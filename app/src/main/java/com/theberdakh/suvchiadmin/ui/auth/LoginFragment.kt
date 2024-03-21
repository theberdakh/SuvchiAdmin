package com.theberdakh.suvchiadmin.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.theberdakh.suvchiadmin.R
import com.theberdakh.suvchiadmin.data.local.SharedPreferences
import com.theberdakh.suvchiadmin.data.remote.auth.models.LoginResponse
import com.theberdakh.suvchiadmin.data.remote.auth.models.UserProfile
import com.theberdakh.suvchiadmin.databinding.FragmentLoginBinding
import com.theberdakh.suvchiadmin.presentation.AuthViewModel
import com.theberdakh.suvchiadmin.utils.getString
import com.theberdakh.suvchiadmin.utils.isNotEmptyOrBlank
import com.theberdakh.suvchiadmin.utils.shakeIfEmptyOrBlank
import com.theberdakh.suvchiadmin.utils.showToast
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.log

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = checkNotNull(_binding)
    private val authViewModel by viewModel<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)


        initViews()
        initListeners()
        initObservers()


        return binding.root
    }

    private fun initObservers() {
        authViewModel.responseLoginIsSuccessful.onEach { loginResponse ->
            if (loginResponse.role == LoginResponse.ROLE_ADMIN) {
                saveLoginResponse(loginResponse)
                requestUserProfile()
            } else {
                showToast(getString(R.string.error_you_are_not_admin))
            }
        }.launchIn(lifecycleScope)

        authViewModel.responseLoginIsMessage.onEach {
            showToast("Message: $it")
        }.launchIn(lifecycleScope)

        authViewModel.responseLoginIsError.onEach {
            it.printStackTrace()
        }.launchIn(lifecycleScope)

        authViewModel.responseProfileIsSuccess.onEach { userProfile ->
            savePassword()
            saveUserProfile(userProfile)
            navigateToMainFragment()
        }.launchIn(lifecycleScope)

        authViewModel.responseProfileIsMessage.onEach {
            showToast("Profile Message: $it")
        }.launchIn(lifecycleScope)

        authViewModel.responseProfileIsError.onEach {
            it.printStackTrace()
        }.launchIn(lifecycleScope)
    }


    private fun savePassword() {
        SharedPreferences().password = binding.passwordEditText.getString()
    }

    private fun saveUserProfile(userProfile: UserProfile) {
        userProfile.apply {
            SharedPreferences().firstName = firstName
            SharedPreferences().lastName = lastName
            SharedPreferences().middleName = middleName
            SharedPreferences().phone = phone
            SharedPreferences().avatar = avatar
            SharedPreferences().username = username
            SharedPreferences().latitude = latitude
            SharedPreferences().longitude = longitude
            SharedPreferences().passport = passport
        }
    }

    private fun navigateToMainFragment() {
        if (SharedPreferences().role == "admin") {
            SharedPreferences().isLoggedIn = true
            val navHostFragment =
                requireActivity().supportFragmentManager.findFragmentById(R.id.fragment_parent_container) as NavHostFragment
            val inflater = navHostFragment.navController.navInflater
            val graph = inflater.inflate(R.navigation.parent_nav)

            navHostFragment.navController.graph = graph
        } else {
            showToast("Siziń akkoutıńız admin statusına iye emes.")
        }


    }

    private fun requestUserProfile() {
        Log.d("LoginFragment", "request user profile ")
        lifecycleScope.launch {
            authViewModel.getUserProfile()
        }
    }

    private fun saveLoginResponse(loginResponse: LoginResponse) {
        SharedPreferences().accessToken = loginResponse.accessToken
        SharedPreferences().refreshToken = loginResponse.refreshToken
        SharedPreferences().role = loginResponse.role
    }

    private fun initListeners() {
        binding.loginButton.setOnClickListener {
            if (binding.usernameEditText.isNotEmptyOrBlank() && binding.passwordEditText.isNotEmptyOrBlank()) {
                login(
                    username = binding.usernameEditText.getString(),
                    password = binding.passwordEditText.getString()
                )
            }
        }
    }

    private fun initViews() {
        binding.usernameEditText.shakeIfEmptyOrBlank()
        binding.passwordEditText.shakeIfEmptyOrBlank()
    }

    private fun login(username: String, password: String) {
        lifecycleScope.launch {
            authViewModel.login(
                username = username,
                password = password
            )
        }
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}