package com.indie.apps.pannypal.data.Paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.indie.apps.pannypal.data.dao.MerchantDao
import com.indie.apps.pannypal.data.module.MerchantNameAndDetails
import kotlinx.coroutines.delay
import javax.inject.Inject

open class BasePagingSource @Inject constructor(
    private val merchantDao: MerchantDao
) : PagingSource<Int, MerchantNameAndDetails>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MerchantNameAndDetails> {
        val page = params.key ?: 0
        return try {
            val entities = merchantDao.searchMerchantNameAndDetailList("",params.loadSize, page * params.loadSize)

            println("aaaa start loading $page")
            delay(5000L)
            println("aaaa end loading")
            LoadResult.Page(
                data = entities,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (entities.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MerchantNameAndDetails>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}