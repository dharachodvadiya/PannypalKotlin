package com.indie.apps.pennypal.repository

import com.indie.apps.pennypal.data.database.db_entity.User
import com.indie.apps.pennypal.data.module.user.UserWithPaymentName
import kotlinx.coroutines.flow.Flow

interface UserRepository : BaseRepository<User> {

    fun getUser(): Flow<User>

    fun getCurrency(): Flow<String>

    fun getCurrencyId(): Flow<Long>

    fun getCurrencyCountryCode(): Flow<String>

    fun getUserWithPaymentName(): Flow<UserWithPaymentName>

    suspend fun updatePayment(paymentId: Long): Int

    suspend fun updateCurrency(currencyCountryCode: String): Int

    suspend fun updateName(name: String): Int

    suspend fun updateLastSyncTime(lastSyncDateInMilli: Long): Int

    //suspend fun updateAmount(incomeAmt: Double, expenseAmt: Double): Int

    suspend fun updateWithDefaultPayment(): Int
}