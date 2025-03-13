package com.indie.apps.pennypal.repository

import com.indie.apps.pennypal.data.database.dao.BaseCurrencyDao
import com.indie.apps.pennypal.data.database.entity.BaseCurrency
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class BaseCurrencyRepositoryImpl @Inject constructor(
    private val baseCurrencyDao: BaseCurrencyDao,
    private val dispatcher: CoroutineDispatcher
) : BaseCurrencyRepository {
    override suspend fun getBaseCurrencyFromCode(countryCode: String) =
        baseCurrencyDao.getBaseCurrencyFromCode(countryCode)

    override suspend fun getBaseCurrencyFromId(id: Long) =
        baseCurrencyDao.getBaseCurrencyFromCode(id)

    override suspend fun insert(obj: BaseCurrency) =
        baseCurrencyDao.insertOrIgnoreQuery(obj.currencyCountryCode)

    override suspend fun update(obj: BaseCurrency) = baseCurrencyDao.update(obj)
}