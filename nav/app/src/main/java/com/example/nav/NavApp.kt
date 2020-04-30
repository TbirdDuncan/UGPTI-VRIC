package com.example.nav

import android.app.Application
import timber.log.Timber

class NavApp: Application() {
    override fun onCreate(){
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}