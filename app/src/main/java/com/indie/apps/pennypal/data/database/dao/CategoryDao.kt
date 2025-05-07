package com.indie.apps.pennypal.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.indie.apps.pennypal.data.database.db_entity.Category
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
    @Query(
        """WITH recent_categories AS (
        SELECT c.*
        FROM category c
        JOIN merchant_data m ON c.id = m.category_id
        WHERE c.soft_delete = 0 AND (c.type == :type OR c.type = 0)
        GROUP BY c.id
        ORDER BY MAX(m.date_milli) DESC
        LIMIT (:limit -1)
    ),
    
    additional_categories AS (
        SELECT * FROM category
        WHERE id NOT IN (SELECT id FROM recent_categories) AND (type == :type OR type = 0)
        AND soft_delete = 0
        ORDER BY id ASC
        LIMIT (:limit - (SELECT COUNT(*) FROM recent_categories))
    )

    SELECT * FROM recent_categories
    UNION ALL
    SELECT * FROM additional_categories
"""
    )
    suspend fun getRecentUsedCategoryList(limit: Int, type: Int): List<Category>

    @Transaction
    @Query("SELECT * FROM category where (type = :type OR type = 0) AND name LIKE  '%' || :searchQuery || '%'  AND soft_delete = 0 ORDER BY id DESC")
    fun searchCategoryFromTypeList(type: Int, searchQuery: String): Flow<List<Category>>

    @Transaction
    @Query("SELECT * FROM category where name LIKE  '%' || :searchQuery || '%' AND soft_delete = 0 ORDER BY id DESC")
    fun searchCategoryList(searchQuery: String): PagingSource<Int, Category>
}