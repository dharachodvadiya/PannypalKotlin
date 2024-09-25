package com.indie.apps.pennypal.data.preference

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

object PreferenceManager {
    private const val PREF_NAME = "MySharedPrefs"
    private lateinit var sharedPreferences: SharedPreferences

    private val INSTANCE: PreferenceManager by lazy { this }

    fun getInstance(context: Context): PreferenceManager {
        if (!::sharedPreferences.isInitialized) {
            sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener)

        }
        return INSTANCE
    }

    // Flow for preference change events
    private val _preferenceChangeFlow = MutableSharedFlow<String>(replay = 1)
    val preferenceChangeFlow: SharedFlow<String> get() = _preferenceChangeFlow

    private val preferenceChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->


            if (key != null) {
                val success = _preferenceChangeFlow.tryEmit(key)
            }
        }

    fun <T> saveData(key: String, value: T) {
        with(sharedPreferences.edit()) {
            when (value) {
                is String -> putString(key, value)
                is Int -> putInt(key, value)
                is Boolean -> putBoolean(key, value)
                else -> throw IllegalArgumentException("Unsupported type")
            }.apply()
        }
    }

    fun <T> getData(key: String, defaultValue: T): T {
        return when (defaultValue) {
            is String -> sharedPreferences.getString(key, defaultValue) as T
            is Int -> sharedPreferences.getInt(key, defaultValue) as T
            is Boolean -> sharedPreferences.getBoolean(key, defaultValue) as T
            // Add more types as needed
            else -> throw IllegalArgumentException("Unsupported type")
        }
    }

}