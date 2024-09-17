package com.indie.apps.pennypal.repository

import com.indie.apps.pennypal.data.entity.Category

interface CategoryRepository : BaseRepository<Category> {
    suspend fun insertCategoryList(categories: List<Category>): List<Long>
}