package com.indie.apps.pannypal

import android.app.Application
import com.indie.apps.pannypal.data.db.AppDatabase

class MyApplication : Application() {

    lateinit var database: AppDatabase

    override fun onCreate() {
        super.onCreate()
        database = AppDatabase.getInstance(this)
    }
}