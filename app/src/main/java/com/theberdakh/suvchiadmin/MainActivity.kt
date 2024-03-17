package com.theberdakh.suvchiadmin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.theberdakh.suvchiadmin.data.local.SharedPreferences
import com.theberdakh.suvchiadmin.utils.setOnlyLightMode
import com.theberdakh.suvchiadmin.utils.showToast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_parent_container) as NavHostFragment
        val inflater = navHostFragment.navController.navInflater

        val graph = if (SharedPreferences().isLoggedIn) {
            inflater.inflate(R.navigation.parent_nav)
        } else {
            inflater.inflate(R.navigation.login_nav)
        }

        navHostFragment.navController.graph = graph


    }
}