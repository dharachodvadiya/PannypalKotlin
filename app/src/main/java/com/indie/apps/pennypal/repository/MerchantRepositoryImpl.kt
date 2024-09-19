package com.indie.apps.pennypal.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.indie.apps.pennypal.data.dao.MerchantDao
import com.indie.apps.pennypal.data.entity.Merchant
import com.indie.apps.pennypal.data.paging.BasePagingSource
import com.indie.apps.pennypal.util.Util
import javax.inject.Inject

class MerchantRepositoryImpl @Inject constructor(private val merchantDao: MerchantDao) :
    MerchantRepository {

    override suspend fun deleteMerchantWithId(id: Long) = merchantDao.deleteMerchantWithId(id)

    override suspend fun deleteMerchantWithIdList(idList: List<Long>) =
        merchantDao.deleteMerchantWithIdList(idList)

    override fun getMerchantList() =
        merchantDao.getMerchantList()

    override fun getMerchantFromId(id: Long) = merchantDao.getMerchantFromId(id)

    /* override suspend fun getTotalIncomeAndeExpenseFromIds(ids: List<Long>) =
         merchantDao.getTotalIncomeAndeExpenseFromIds(ids)*/

    /* override suspend fun updateAmountWithDate(
         id: Long,
         incomeAmt: Double,
         expenseAmt: Double
     ) = merchantDao.updateAmountWithDate(id, incomeAmt, expenseAmt)*/

    /*override suspend fun addAmountWithDate(
        id: Long,
        incomeAmt: Double,
        expenseAmt: Double,
        dateInMilli: Long
    ) = merchantDao.addAmountWithDate(id, incomeAmt, expenseAmt, dateInMilli)*/

    override fun searchMerchantNameAndDetailList(
        searchQuery: String
    ) = merchantDao.searchMerchantNameAndDetailList(searchQuery)

    override fun getRecentMerchantNameAndDetailList() =
        merchantDao.getRecentMerchantNameAndDetailList()

    override fun searchMerchantNameAndDetailListPaging(
        searchQuery: String
    ) = Pager(
        config = PagingConfig(
            pageSize = Util.PAGE_SIZE,
            prefetchDistance = Util.PAGE_PREFETCH_DISTANCE
        ),
        pagingSourceFactory = { BasePagingSource(merchantDao) }
    ).flow

    override fun searchMerchantList(
        searchQuery: String
    ) = merchantDao.searchMerchantList(searchQuery)

    override suspend fun insert(obj: Merchant) = merchantDao.insert(obj)

    override suspend fun update(obj: Merchant) = merchantDao.update(obj)
}