package com.indie.apps.pennypal.repository

import com.indie.apps.pennypal.data.entity.User

interface UserRepository : BaseRepository<User> {

    suspend fun getUser(): User

    suspend fun updateAmount(incomeAmt: Double, expenseAmt: Double): Int
}