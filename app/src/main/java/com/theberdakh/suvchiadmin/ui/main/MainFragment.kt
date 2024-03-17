package com.theberdakh.suvchiadmin.ui.main

import android.net.http.HttpException
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresExtension
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.theberdakh.suvchiadmin.R
import com.theberdakh.suvchiadmin.data.local.SharedPreferences
import com.theberdakh.suvchiadmin.data.remote.auth.AuthApi
import com.theberdakh.suvchiadmin.data.remote.auth.models.LoginRequest
import com.theberdakh.suvchiadmin.data.remote.auth.models.LoginResponse
import com.theberdakh.suvchiadmin.databinding.FragmentMainBinding
import com.theberdakh.suvchiadmin.presentation.AuthViewModel
import com.theberdakh.suvchiadmin.ui.contracts.ContractsFragment
import com.theberdakh.suvchiadmin.ui.dashboard.DashboardFragment
import com.theberdakh.suvchiadmin.ui.settings.SettingsFragment
import com.theberdakh.suvchiadmin.utils.TokenInterceptor
import com.theberdakh.suvchiadmin.utils.replaceFragment
import com.theberdakh.suvchiadmin.utils.showToast
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

const val TAG = "MainFragment"
class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val authViewModel by viewModel<AuthViewModel>()
    private val binding get() = checkNotNull(_binding)

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        initObservers()

        initBottomNavigation()

        return binding.root
    }

    private fun initBottomNavigation() {
        //to set default fragment when no menu item clicked
        replaceFragment(
            childFragmentManager,
            R.id.nested_fragment_container,
            DashboardFragment()
        )

        binding.bottomNavView.setOnItemSelectedListener { menuItem ->
            val nestedFragment = when (menuItem.itemId) {
                R.id.action_bottom_dashboard -> DashboardFragment()
                R.id.action_bottom_contracts -> ContractsFragment()
                R.id.action_bottom_account -> SettingsFragment()
                else -> throw Exception("Not found nested fragment")
            }

            replaceFragment(childFragmentManager, R.id.nested_fragment_container, nestedFragment)

            true
        }


    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    private fun initObservers() {
        //Manual handling of refresh token when access token revokes
        lifecycleScope.launch {
          val response =  try {
              RetrofitInstance.api.login(LoginRequest(SharedPreferences().username, SharedPreferences().password))
          } catch (e: IOException){
              showToast("Internet baylanısıńızdı tekseriń")
              Log.d(TAG, "IOException (check internet)")
              return@launch
          } catch (e: HttpException){
              Log.d(TAG, "HttpException")
              return@launch
          }
            if (response.isSuccessful && response.body() != null){
                Log.d(TAG, "Refresh Token: ${response.body()!!.refreshToken}")
                refreshTokens(response.body()!!)
            } else{
                showToast("Maǵlıwmatlardı alıwıńız ushın qayta login isleń")
            }

        }

        authViewModel.responseLoginIsSuccessful.onEach {
            Log.d("MainFragment", "Response token ${it.accessToken}")
            SharedPreferences().accessToken = it.accessToken
            SharedPreferences().refreshToken = it.refreshToken

        }.launchIn(lifecycleScope)
    }

    private fun refreshTokens(loginResponse: LoginResponse) {
        SharedPreferences().accessToken = loginResponse.accessToken
        SharedPreferences().refreshToken = loginResponse.refreshToken
    }


    private object RetrofitInstance {

        val httpLoggingInterceptor = HttpLoggingInterceptor().setLevel(
            HttpLoggingInterceptor.Level.BODY
        )

        val client = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor { chain: Interceptor.Chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer ${SharedPreferences().refreshToken}")
                    .build()
             chain.proceed(request)
            }
            .build()

        val api: AuthApi by lazy {
            Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.smartwaterdegree.uz")
                .client(client)
                .build()
                .create(AuthApi::class.java)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}