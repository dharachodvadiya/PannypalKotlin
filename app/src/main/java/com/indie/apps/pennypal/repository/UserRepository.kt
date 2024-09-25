package com.indie.apps.pennypal.repository

import com.indie.apps.pennypal.data.database.entity.User
import com.indie.apps.pennypal.data.module.UserWithPaymentName
import kotlinx.coroutines.flow.Flow

interface UserRepository : BaseRepository<User> {

    fun getUser(): Flow<User>

    fun getUserWithPaymentName(): Flow<UserWithPaymentName>

    suspend fun updatePayment(paymentId: Long): Int

    suspend fun updateCurrency(currency : String, currencyCountryCode : String): Int

    //suspend fun updateAmount(incomeAmt: Double, expenseAmt: Double): Int

    suspend fun updateWithDefaultPayment(): Int
}