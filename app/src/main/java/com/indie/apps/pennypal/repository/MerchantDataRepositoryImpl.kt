package com.indie.apps.pennypal.repository

import com.indie.apps.pennypal.data.dao.MerchantDataDao
import com.indie.apps.pennypal.data.entity.MerchantData
import javax.inject.Inject

class MerchantDataRepositoryImpl @Inject constructor(private val merchantDataDao: MerchantDataDao) :
    MerchantDataRepository {

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
        id: Long,
    ) = merchantDataDao.getMerchantDataListFromMerchantId(id)

    override fun getMerchantsDataWithMerchantNameList(
        timeZoneOffsetInMilli: Int
    ) = merchantDataDao.getMerchantsDataWithMerchantNameList(timeZoneOffsetInMilli)

    override fun searchMerchantDataWithMerchantNameList(
        searchQuery: String,
        timeZoneOffsetInMilli: Int
    ) = merchantDataDao.searchMerchantDataWithMerchantNameList(searchQuery, timeZoneOffsetInMilli)

    override fun getMerchantDataDailyTotalList(
        timeZoneOffsetInMilli: Int
    ) = merchantDataDao.getMerchantDataDailyTotalList(timeZoneOffsetInMilli)


    override suspend fun insert(merchantData: MerchantData) = merchantDataDao.insert(merchantData)

    override suspend fun update(merchantData: MerchantData) = merchantDataDao.update(merchantData)
}