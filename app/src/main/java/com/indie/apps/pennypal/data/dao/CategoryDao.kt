package com.indie.apps.pennypal.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.indie.apps.pennypal.data.entity.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao : BaseDao<Category> {
    @Insert
    suspend fun insertCategoryList(payments: List<Category>): List<Long>

    @Transaction
    @Query("SELECT * FROM category where id = :id")
    fun getCategoryFromId(id: Long): Flow<Category>

    @Transaction
    @Query("SELECT * FROM category where type = :type OR type = 0")
    fun getCategoryFromTypeList(type: Int): Flow<List<Category>>
}