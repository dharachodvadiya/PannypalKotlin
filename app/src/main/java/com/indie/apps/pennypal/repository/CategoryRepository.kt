package com.indie.apps.pennypal.repository

import androidx.paging.PagingSource
import com.indie.apps.pennypal.data.database.entity.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository : BaseRepository<Category> {
    suspend fun insertCategoryList(categories: List<Category>): List<Long>

    fun getCategoryFromId(id: Long): Flow<Category>

    fun getCategoryFromTypeList(type: Int): Flow<List<Category>>
    fun searchCategoryFromTypeList(type: Int, searchQuery: String): Flow<List<Category>>
    fun searchCategoryList(searchQuery: String): PagingSource<Int, Category>
}