package com.indie.apps.pennypal.repository

import com.indie.apps.pennypal.data.database.dao.CategoryDao
import com.indie.apps.pennypal.data.database.entity.Category
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao,
    private val dispatcher: CoroutineDispatcher
) :
    CategoryRepository {
    override suspend fun insertCategoryList(categories: List<Category>) =
        categoryDao.insertCategoryList(categories)

    override fun getCategoryFromId(id: Long) = categoryDao.getCategoryFromId(id).flowOn(dispatcher)

    override fun getCategoryFromTypeList(type: Int) =
        categoryDao.getCategoryFromTypeList(type).flowOn(dispatcher)

    override fun searchCategoryFromTypeList(type: Int, searchQuery: String) =
        categoryDao.searchCategoryFromTypeList(type, searchQuery).flowOn(dispatcher)

    override suspend fun insert(obj: Category) = categoryDao.insert(obj)

    override suspend fun update(obj: Category) = categoryDao.update(obj)

}