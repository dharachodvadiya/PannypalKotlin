package com.indie.apps.pennypal

import android.app.Application
import com.indie.apps.cpp.data.loadCountryData
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application()
{
    override fun onCreate() {
        super.onCreate()

        loadCountryData(this)
    }
}