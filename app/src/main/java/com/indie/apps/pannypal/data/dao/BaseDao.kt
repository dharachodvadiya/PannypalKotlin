package com.indie.apps.pannypal.data.dao

import androidx.room.Insert
import androidx.room.Update

interface BaseDao<T> {
    @Insert
    suspend fun insert(obj: T) : Long

    @Update
    suspend fun update(obj: T) : Int
}