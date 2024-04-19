package com.theberdakh.suvchiadmin.data.remote.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.InetAddress
import java.net.UnknownHostException

fun Context.isOnline(): Boolean {
    val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    if (capabilities != null) {
        when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
        }
    }
    return false
}
suspend fun checkHostResolution(context: Context, hostname: String): Boolean {
    val isResolved: Boolean = try {
        val address = withContext(Dispatchers.IO) {
            InetAddress.getByName(hostname)
        }
        true
    } catch (e: UnknownHostException) {
        Toast.makeText(context, "Xosting menen baylanısa almadı. Iltimas, internet baylanısıńızdı tekseriń.", Toast.LENGTH_LONG).show()
        false
    }
    return isResolved
}


