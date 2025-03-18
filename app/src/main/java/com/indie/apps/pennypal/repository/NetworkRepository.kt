package com.indie.apps.pennypal.repository

interface NetworkRepository {
    suspend fun isNetworkAvailable(): Boolean
}

