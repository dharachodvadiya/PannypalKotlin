package com.indie.apps.pennypal.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.indie.apps.pennypal.data.database.entity.Category
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

    @Transaction
    @Query("SELECT * FROM category where (type = :type OR type = 0) AND name LIKE  '%' || :searchQuery || '%'")
    fun searchCategoryFromTypeList(type: Int, searchQuery: String): Flow<List<Category>>

    @Transaction
    @Query("SELECT * FROM category where name LIKE  '%' || :searchQuery || '%'")
    fun searchCategoryList(searchQuery: String): PagingSource<Int, Category>
}