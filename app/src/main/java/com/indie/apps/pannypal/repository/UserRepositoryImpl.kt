package com.indie.apps.pannypal.repository

import com.indie.apps.pannypal.data.dao.UserDao
import com.indie.apps.pannypal.data.entity.User

class UserRepositoryImpl(private val userDao: UserDao) : UserRepository {

    override suspend fun getUser() = userDao.getUser()

    override suspend fun insert(user: User) = userDao.insert(user)

    override suspend fun update(user: User) = userDao.update(user)
}