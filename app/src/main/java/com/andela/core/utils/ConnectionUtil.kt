package com.andela.core.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class ConnectionUtil(private val _context: Context) {

    val isConnectingToInternet: Boolean
        get() {
            val connectivity = _context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val info = connectivity.allNetworkInfo
            for (anInfo in info)
                if (anInfo.state == NetworkInfo.State.CONNECTED) {
                    return true
                }
            return false
        }
}