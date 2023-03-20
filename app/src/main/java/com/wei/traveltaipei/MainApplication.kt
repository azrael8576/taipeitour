package com.wei.traveltaipei

import android.app.Application
import android.content.Context
import com.facebook.stetho.Stetho

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        MainApplication.appContext = applicationContext
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }

    companion object {
        lateinit var appContext: Context
    }
}
