package com.remotearthsolutions.expensetracker.services

import android.app.Service
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class InternetCheckerServiceImpl(private val context: Context) :
    InternetCheckerService {
    override val isConnected: Boolean
        get() {
            val connectivity =
                context.getSystemService(Service.CONNECTIVITY_SERVICE) as ConnectivityManager
            val info = connectivity.activeNetworkInfo
            if (info != null) {
                return info.state == NetworkInfo.State.CONNECTED
            }
            return false
        }

}