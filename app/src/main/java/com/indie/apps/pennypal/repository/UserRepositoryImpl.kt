package com.indie.apps.pennypal.repository

import com.indie.apps.cpp.data.repository.CountryRepository
import com.indie.apps.pennypal.data.database.dao.UserDao
import com.indie.apps.pennypal.data.database.entity.BaseCurrency
import com.indie.apps.pennypal.data.database.entity.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val countryRepository: CountryRepository,
    private val baseCurrencyRepository: BaseCurrencyRepository,
    private val dispatcher: CoroutineDispatcher
) : UserRepository {

    override fun getUser() = userDao.getUser().flowOn(dispatcher)
    override fun getCurrency() = userDao.getCurrencyCountryCode()
        .map { countryRepository.getCurrencySymbolFromCountryCode(it) }.flowOn(dispatcher)

    override fun getCurrencyCountryCode() = userDao.getCurrencyCountryCode().flowOn(dispatcher)

    override fun getUserWithPaymentName() = userDao.getUserWithPaymentName().flowOn(dispatcher)
    override suspend fun updatePayment(paymentId: Long) = userDao.updatePayment(paymentId)

    override suspend fun updateCurrency(currencyCountryCode: String): Int {
        val updateCount = userDao.updateCurrency(currencyCountryCode)

        if (updateCount > 0)
            baseCurrencyRepository.insert(BaseCurrency(currencyCountryCode = currencyCountryCode))
        return updateCount

    }


    override suspend fun updateName(name: String) =
        userDao.updateName(name)

    override suspend fun updateLastSyncTime(lastSyncDateInMilli: Long) =
        userDao.updateLastSyncTime(lastSyncDateInMilli)

    /* override suspend fun updateAmount(incomeAmt: Double, expenseAmt: Double) =
         userDao.updateAmount(incomeAmt, expenseAmt)*/

    override suspend fun updateWithDefaultPayment() = userDao.updateWithDefaultPayment()

    override suspend fun insert(obj: User): Long {
        val insertId = userDao.insert(obj)
        if (insertId > 0)
            baseCurrencyRepository.insert(BaseCurrency(currencyCountryCode = obj.currencyCountryCode))

        return insertId
    }

    override suspend fun update(obj: User) = userDao.update(obj)
}