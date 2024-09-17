package com.indie.apps.pennypal.data.dao

import androidx.room.Dao
import androidx.room.Insert
import com.indie.apps.pennypal.data.entity.Category

@Dao
interface CategoryDao : BaseDao<Category> {
    @Insert
    suspend fun insertCategoryList(payments: List<Category>): List<Long>
}