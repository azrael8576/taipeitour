package com.wei.taipeitour.utilities

import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

const val FACEBOOK_NEWER_VERSION = 3002850

/**
 * 判断当前网络是否可用(6.0以上版本)
 * 实时
 * @param context
 * @return
 */
fun isNetSystemUsable(context: Context): Boolean {
    var isNetUsable = false
    val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networkCapabilities = manager.getNetworkCapabilities(manager.activeNetwork)
        if (networkCapabilities != null) {
            isNetUsable =
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        }
    }
    return isNetUsable
}

fun isAppInstalled(context: Context): Boolean {
    return try {
        context.packageManager
            .getApplicationInfo("com.facebook.katana", 0).enabled
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}

// method to get the right URL to use in the intent
fun getFacebookPageURL(context: Context, fbUrl: String): String {
    val packageManager = context.packageManager
    return try {
        val versionCode = packageManager.getPackageInfo("com.facebook.orca", 0).versionCode
        if (versionCode >= FACEBOOK_NEWER_VERSION) { // newer versions of fb app
            "fb://facewebmodal/f?href=$fbUrl"
        } else { // older versions of fb app
            var fbPageId = fbUrl.replace("https://www.facebook.com/", "").replace("/", "")
            "fb://page/$fbPageId"
        }
    } catch (e: PackageManager.NameNotFoundException) {
        fbUrl
    }
}
