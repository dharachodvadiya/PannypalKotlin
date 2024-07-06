package com.indie.apps.pannypal.data.dao

import androidx.room.Insert
import androidx.room.Update

interface BaseDao<T> {
    @Insert
    fun insert(obj: T) : Long

    @Update
    fun update(obj: T) : Int
}