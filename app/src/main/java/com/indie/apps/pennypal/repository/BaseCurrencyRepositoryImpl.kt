package com.indie.apps.pennypal.repository

import com.indie.apps.pennypal.data.database.dao.BaseCurrencyDao
import com.indie.apps.pennypal.data.database.entity.BaseCurrency
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class BaseCurrencyRepositoryImpl @Inject constructor(
    private val baseCurrencyDao: BaseCurrencyDao,
    private val dispatcher: CoroutineDispatcher
) : BaseCurrencyRepository {

    override suspend fun insert(obj: BaseCurrency) = baseCurrencyDao.insert(obj)

    override suspend fun update(obj: BaseCurrency) = baseCurrencyDao.update(obj)
}