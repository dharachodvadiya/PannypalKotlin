package com.indie.apps.pennypal.repository

import com.indie.apps.pennypal.data.entity.User
import kotlinx.coroutines.flow.Flow

interface UserRepository : BaseRepository<User> {

    fun getUser(): Flow<User>

    suspend fun updateAmount(incomeAmt: Double, expenseAmt: Double): Int
}