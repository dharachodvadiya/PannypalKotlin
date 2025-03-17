package com.indie.apps.pennypal.repository

import com.indie.apps.pennypal.data.database.dao.BaseCurrencyDao
import com.indie.apps.pennypal.data.database.entity.BaseCurrency
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BaseCurrencyRepositoryImpl @Inject constructor(
    private val baseCurrencyDao: BaseCurrencyDao,
    private val dispatcher: CoroutineDispatcher
) : BaseCurrencyRepository {
    override suspend fun getBaseCurrencyFromCode(countryCode: String) =
        withContext(dispatcher) { baseCurrencyDao.getBaseCurrencyFromCode(countryCode) }

    override suspend fun getBaseCurrencyFromId(id: Long) =
        withContext(dispatcher) { baseCurrencyDao.getBaseCurrencyFromCode(id) }

    override suspend fun insert(data: BaseCurrency) =
        withContext(dispatcher) {
            val id = baseCurrencyDao.insertOrIgnoreQuery(data)
            id
        }

}