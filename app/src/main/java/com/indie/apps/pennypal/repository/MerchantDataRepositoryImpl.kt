package com.indie.apps.pennypal.repository

import androidx.paging.PagingSource
import com.indie.apps.pennypal.data.dao.MerchantDataDao
import com.indie.apps.pennypal.data.entity.MerchantData
import com.indie.apps.pennypal.data.module.MerchantDataWithAllData
import javax.inject.Inject

class MerchantDataRepositoryImpl @Inject constructor(private val merchantDataDao: MerchantDataDao) :
    MerchantDataRepository {
    override suspend fun updateMerchantDataPaymentId(oldPaymentId: Long, newPaymentId: Long) =
        merchantDataDao.updateMerchantDataPaymentId(oldPaymentId, newPaymentId)

    override suspend fun deleteMerchantDataWithId(id: Long): Int =
        merchantDataDao.deleteMerchantDataWithId(id)

    override suspend fun deleteMerchantDataWithIdList(idList: List<Long>) =
        merchantDataDao.deleteMerchantDataWithIdList(idList)

    override suspend fun deleteMerchantDataWithMerchantIdList(idList: List<Long>) =
        merchantDataDao.deleteMerchantDataWithMerchantIdList(idList)

    override suspend fun deleteMerchantDataWithMerchantId(id: Long) =
        merchantDataDao.deleteMerchantDataWithMerchantId(id)

    override suspend fun getTotalIncomeAndeExpenseFromIds(ids: List<Long>) =
        merchantDataDao.getTotalIncomeAndeExpenseFromIds(ids)

    override suspend fun getMerchantDataFromId(id: Long) = merchantDataDao.getMerchantDataFromId(id)

    override fun getMerchantDataList() = merchantDataDao.getMerchantDataList()

    override fun getMerchantDataListFromMerchantId(
        merchantId: Long,
    ) = merchantDataDao.getMerchantDataListFromMerchantId(merchantId)

    override fun searchMerchantsDataWithAllDataList(
        searchQuery : String
    ) = merchantDataDao.searchMerchantsDataWithAllDataList(searchQuery)

    override fun getRecentMerchantsDataWithAllDataList() =
        merchantDataDao.getRecentMerchantsDataWithAllDataList()

    override fun getMerchantsDataWithPaymentNameListFromMerchantId(
        merchantId: Long) =
        merchantDataDao.getMerchantsDataWithPaymentNameListFromMerchantId(merchantId)

    override fun searchMerchantDataWithMerchantNameList(
        searchQuery: String,
        timeZoneOffsetInMilli: Int
    ) = merchantDataDao.searchMerchantDataWithMerchantNameList(searchQuery, timeZoneOffsetInMilli)

    override fun getMerchantDataDailyTotalList(
        timeZoneOffsetInMilli: Int
    ) = merchantDataDao.getMerchantDataDailyTotalList(timeZoneOffsetInMilli)

    override suspend fun insert(obj: MerchantData) = merchantDataDao.insert(obj)

    override suspend fun update(obj: MerchantData) = merchantDataDao.update(obj)

    override fun getMerchantDataWithNameWithDayTotal(timeZoneOffsetInMilli: Int) =
        merchantDataDao.getCombinedDataWithLimit(timeZoneOffsetInMilli)
}