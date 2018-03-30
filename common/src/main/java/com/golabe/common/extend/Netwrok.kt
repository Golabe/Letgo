package com.golabe.common.extend

import android.content.Context
import android.net.ConnectivityManager

class Netwrok {

    fun Context.isNetwork(): Boolean {
        val manager = this.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (manager == null) false
        val networkInfo = manager.activeNetworkInfo
        return !(networkInfo == null || !networkInfo.isAvailable)

    }
}