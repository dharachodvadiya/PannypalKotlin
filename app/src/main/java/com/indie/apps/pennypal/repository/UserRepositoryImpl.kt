package com.indie.apps.pennypal.repository

import com.indie.apps.pennypal.data.dao.UserDao
import com.indie.apps.pennypal.data.entity.User
import com.indie.apps.pennypal.data.module.UserWithPaymentName
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val userDao: UserDao) : UserRepository {

    override fun getUser() = userDao.getUser()
    
    override fun getUserWithPaymentName() = userDao.getUserWithPaymentName()
    override suspend fun updatePayment(paymentId: Long) = userDao.updatePayment(paymentId)

    override suspend fun updateCurrency(currency: String) = userDao.updateCurrency(currency)

    override suspend fun updateAmount(incomeAmt: Double, expenseAmt: Double) =
        userDao.updateAmount(incomeAmt, expenseAmt)

    override suspend fun updateWithDefaultPayment() = userDao.updateWithDefaultPayment()

    override suspend fun insert(obj: User) = userDao.insert(obj)

    override suspend fun update(obj: User) = userDao.update(obj)
}