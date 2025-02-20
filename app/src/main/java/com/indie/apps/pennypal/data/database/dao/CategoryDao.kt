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

    @Query("UPDATE category SET soft_delete = 1 WHERE id = :categoryId")
    suspend fun softDeleteCategory(categoryId: Long): Int

    @Query("UPDATE category SET soft_delete = 1 WHERE id IN (:idList)")
    suspend fun softDeleteCategoryList(idList: List<Long>): Int

    @Transaction
    @Query("SELECT * FROM category where name = :name AND soft_delete = 1")
    suspend fun getSoftDeletedCategoryFromName(name: String): Category?

    @Transaction
    @Query("SELECT * FROM category where id = :id")
    fun getCategoryFromId(id: Long): Flow<Category>

    @Transaction
    @Query("SELECT * FROM category where (type = :type OR type = 0) AND soft_delete = 0 ORDER BY id DESC")
    fun getCategoryFromTypeList(type: Int): Flow<List<Category>>

    @Transaction
    @Query("SELECT * FROM category where (type = :type OR type = 0) AND name LIKE  '%' || :searchQuery || '%'  AND soft_delete = 0 ORDER BY id DESC")
    fun searchCategoryFromTypeList(type: Int, searchQuery: String): Flow<List<Category>>

    @Transaction
    @Query("SELECT * FROM category where name LIKE  '%' || :searchQuery || '%' AND soft_delete = 0 ORDER BY id DESC")
    fun searchCategoryList(searchQuery: String): PagingSource<Int, Category>
}