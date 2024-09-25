package com.indie.apps.pennypal.repository

import com.indie.apps.pennypal.data.preference.PreferenceManager
import javax.inject.Inject

class PreferenceRepositoryImpl @Inject constructor(private val preferenceManager: PreferenceManager) :
    PreferenceRepository {

    override fun putString(key: String, value: String) = preferenceManager.saveData(key, value)

    override fun getString(key: String, defaultValue: String) =
        preferenceManager.getData(key, defaultValue)

    override fun putInt(key: String, value: Int) = preferenceManager.saveData(key, value)

    override fun getInt(key: String, defaultValue: Int) =
        preferenceManager.getData(key, defaultValue)

    override fun putBoolean(key: String, value: Boolean) = preferenceManager.saveData(key, value)

    override fun getBoolean(key: String, defaultValue: Boolean) =
        preferenceManager.getData(key, defaultValue)

    override fun preferenceChangeListener() = preferenceManager.preferenceChangeFlow

}