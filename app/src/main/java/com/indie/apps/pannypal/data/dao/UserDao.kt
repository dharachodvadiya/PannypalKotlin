package com.indie.apps.pannypal.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.indie.apps.pannypal.data.entity.User

@Dao
interface UserDao : BaseDao<User>{

    //insert user data
    //update data.....this will also update when merchant update [merchant -> user]
    //get user data
    //can not delete

    @Query("SELECT * FROM user WHERE id = 1")
    fun getUser(): User
}