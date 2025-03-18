package com.indie.apps.pennypal.repository

import com.indie.apps.pennypal.data.service.NetworkMonitor
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject


class NetworkRepositoryImpl @Inject constructor(
    private val networkMonitor: NetworkMonitor,
    private val dispatcher: CoroutineDispatcher
) : NetworkRepository {
    override suspend fun isNetworkAvailable() = networkMonitor.isNetworkAvailable()
}