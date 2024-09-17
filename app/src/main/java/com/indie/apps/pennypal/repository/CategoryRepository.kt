package com.indie.apps.pennypal.repository

import com.indie.apps.pennypal.data.entity.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository : BaseRepository<Category> {
    suspend fun insertCategoryList(categories: List<Category>): List<Long>

    fun getCategoryFromId(id: Long): Flow<Category>

    fun getCategoryFromTypeList(type: Int): Flow<List<Category>>
}