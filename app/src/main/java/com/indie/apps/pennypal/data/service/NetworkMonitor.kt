package com.indie.apps.pennypal.data.service

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.InetAddress
import javax.inject.Inject

class NetworkMonitor @Inject constructor(
    context: Context
) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    suspend fun isNetworkAvailable(): Boolean = withContext(Dispatchers.IO) {
        val activeNetwork = connectivityManager.activeNetwork
        val capabilities = activeNetwork?.let { connectivityManager.getNetworkCapabilities(it) }

        val isConnected = activeNetwork != null &&
                capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true &&
                capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) == true

        // If VPN is active but you want to verify real internet, add a ping test
        if (isConnected && capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_VPN) == true) {
            val hasRealInternet = pingInternet()
            hasRealInternet
        } else {
            isConnected
        }
    }

    private suspend fun pingInternet(): Boolean = withContext(Dispatchers.IO) {
        try {
            val socket =
                InetAddress.getByName("8.8.8.8").isReachable(1000) // Ping Google DNS, 1s timeout
            socket
        } catch (e: Exception) {
            false
        }
    }
}