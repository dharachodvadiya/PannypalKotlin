package com.indie.apps.pannypal.repository

import com.indie.apps.pannypal.data.entity.User

interface UserRepository : BaseRepository<User>{

    suspend fun getUser() : User
}