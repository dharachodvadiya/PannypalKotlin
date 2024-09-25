package com.indie.apps.pennypal.repository

import com.indie.apps.pennypal.data.database.dao.CategoryDao
import com.indie.apps.pennypal.data.database.entity.Category
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(private val categoryDao: CategoryDao) :
    CategoryRepository {
    override suspend fun insertCategoryList(categories: List<Category>) =
        categoryDao.insertCategoryList(categories)

    override fun getCategoryFromId(id: Long) = categoryDao.getCategoryFromId(id)

    override fun getCategoryFromTypeList(type: Int) = categoryDao.getCategoryFromTypeList(type)

    override suspend fun insert(obj: Category) = categoryDao.insert(obj)

    override suspend fun update(obj: Category) = categoryDao.update(obj)

}