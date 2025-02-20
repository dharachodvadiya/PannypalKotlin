package com.indie.apps.pennypal.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.indie.apps.pennypal.data.database.dao.MerchantDao
import com.indie.apps.pennypal.data.database.entity.Merchant
import com.indie.apps.pennypal.data.paging.BasePagingSource
import com.indie.apps.pennypal.util.Util
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MerchantRepositoryImpl @Inject constructor(
    private val merchantDao: MerchantDao,
    private val dispatcher: CoroutineDispatcher
) :
    MerchantRepository {

    override suspend fun deleteMerchantWithId(id: Long) = merchantDao.softDeleteMerchantWithId(id)

    override suspend fun deleteMerchantWithIdList(idList: List<Long>) =
        merchantDao.softDeleteMerchantWithIdList(idList)

    override fun getMerchantList() =
        merchantDao.getMerchantList()

    override fun getMerchantFromId(id: Long) = merchantDao.getMerchantFromId(id).flowOn(dispatcher)

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
        merchantDao.getRecentMerchantNameAndDetailList().flowOn(dispatcher)

    override fun searchMerchantNameAndDetailListPaging(
        searchQuery: String
    ) = Pager(
        config = PagingConfig(
            pageSize = Util.PAGE_SIZE,
            prefetchDistance = Util.PAGE_PREFETCH_DISTANCE
        ),
        pagingSourceFactory = { BasePagingSource(merchantDao) }
    ).flow.flowOn(dispatcher)

    override fun searchMerchantList(
        searchQuery: String
    ) = merchantDao.searchMerchantList(searchQuery)

    override suspend fun insert(obj: Merchant): Long {
        return try {
            merchantDao.insert(obj)
        } catch (e: Exception) {
            val merchant = merchantDao.getSoftDeletedMerchantFromName(obj.name)
            if (merchant != null) {
                if (merchantDao.update(obj.copy(id = merchant.id)) > 0)
                    merchant.id
                else
                    -1
            } else
                throw Exception(e)

        }
    }

    override suspend fun update(obj: Merchant) = merchantDao.update(obj)
}