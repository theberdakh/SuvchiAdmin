package com.theberdakh.suvchiadmin.data.local

import android.content.Context
import android.content.SharedPreferences
import com.theberdakh.suvchiadmin.app.App
import com.theberdakh.suvchiadmin.utils.BooleanPreference
import com.theberdakh.suvchiadmin.utils.IntPreference
import com.theberdakh.suvchiadmin.utils.StringPreference

class SharedPreferences {


    companion object {
        fun clearUserData() {
             App.instance.deleteSharedPreferences(Constants.SHARED_PREFERENCE_NAME)
        }

        private val pref = App.instance.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    var isLoggedIn by BooleanPreference(pref)

    var password by StringPreference(pref)

    var accessToken by StringPreference(pref)
    var refreshToken by StringPreference(pref)
    var role by StringPreference(pref)

    var firstName by StringPreference(pref)
    var lastName by StringPreference(pref)
    var middleName by StringPreference(pref)
    var phone by StringPreference(pref)
    var avatar by StringPreference(pref)
    var username by StringPreference(pref)
    var latitude by StringPreference(pref)
    var longitude by StringPreference(pref)
    var passport by StringPreference(pref)


}