package com.indie.apps.pennypal.repository

import com.indie.apps.pennypal.data.dao.UserDao
import com.indie.apps.pennypal.data.entity.User
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val userDao: UserDao) : UserRepository {

    override suspend fun getUser() = userDao.getUser()

    override suspend fun updateAmount(incomeAmt: Double, expenseAmt: Double) =
        userDao.updateAmount(incomeAmt, expenseAmt)

    override suspend fun insert(user: User) = userDao.insert(user)

    override suspend fun update(user: User) = userDao.update(user)
}