package com.indie.apps.pennypal.repository

import com.indie.apps.pennypal.data.database.dao.CategoryDao
import com.indie.apps.pennypal.data.database.entity.Category
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao,
    private val dispatcher: CoroutineDispatcher
) :
    CategoryRepository {
    override suspend fun insertCategoryList(categories: List<Category>) = withContext(dispatcher) {
        categoryDao.insertCategoryList(categories)
    }

    override suspend fun deleteMultipleCategory(idList: List<Long>) = withContext(dispatcher) {
        categoryDao.softDeleteCategoryList(idList)
    }

    override suspend fun deleteCategory(id: Long) = withContext(dispatcher) {
        categoryDao.softDeleteCategory(
            id
        )
    }

    override fun getCategoryFromId(id: Long) = categoryDao.getCategoryFromId(id).flowOn(dispatcher)

    override fun getCategoryFromTypeList(type: Int) =
        categoryDao.getCategoryFromTypeList(type).flowOn(dispatcher)

    override suspend fun getRecentUsedCategoryList(limit: Int) = withContext(dispatcher) {
        categoryDao.getRecentUsedCategoryList(limit)
    }

    override fun searchCategoryFromTypeList(type: Int, searchQuery: String) =
        categoryDao.searchCategoryFromTypeList(type, searchQuery.trim()).flowOn(dispatcher)

    override fun searchCategoryList(searchQuery: String) =
        categoryDao.searchCategoryList(searchQuery.trim())

    override suspend fun insert(obj: Category) = withContext(dispatcher) {
        try {
            categoryDao.insert(obj)
        } catch (e: Exception) {
            val category = categoryDao.getSoftDeletedCategoryFromName(obj.name)
            if (category != null) {
                if (categoryDao.update(obj.copy(id = category.id)) > 0)
                    category.id
                else
                    -1
            } else
                throw Exception(e)

        }
    }

    override suspend fun update(obj: Category) = withContext(dispatcher) { categoryDao.update(obj) }

}