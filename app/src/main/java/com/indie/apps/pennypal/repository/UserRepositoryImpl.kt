package com.indie.apps.pennypal.repository

import com.indie.apps.pennypal.data.database.dao.UserDao
import com.indie.apps.pennypal.data.database.entity.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val dispatcher: CoroutineDispatcher
) : UserRepository {

    override fun getUser() = userDao.getUser().flowOn(dispatcher)

    override fun getUserWithPaymentName() = userDao.getUserWithPaymentName().flowOn(dispatcher)
    override suspend fun updatePayment(paymentId: Long) = userDao.updatePayment(paymentId)

    override suspend fun updateCurrency(currency: String, currencyCountryCode: String) =
        userDao.updateCurrency(currency, currencyCountryCode)

    override suspend fun updateLastSyncTime(lastSyncDateInMilli: Long) =
        userDao.updateLastSyncTime(lastSyncDateInMilli)

    /* override suspend fun updateAmount(incomeAmt: Double, expenseAmt: Double) =
         userDao.updateAmount(incomeAmt, expenseAmt)*/

    override suspend fun updateWithDefaultPayment() = userDao.updateWithDefaultPayment()

    override suspend fun insert(obj: User) = userDao.insert(obj)

    override suspend fun update(obj: User) = userDao.update(obj)
}