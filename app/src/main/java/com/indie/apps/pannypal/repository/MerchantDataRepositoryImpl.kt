package com.indie.apps.pannypal.repository

import androidx.paging.PagingSource
import com.indie.apps.pannypal.data.dao.MerchantDataDao
import com.indie.apps.pannypal.data.entity.MerchantData
import com.indie.apps.pannypal.data.module.MerchantDataDailyTotal
import com.indie.apps.pannypal.data.module.MerchantDataWithName
import javax.inject.Inject

class MerchantDataRepositoryImpl @Inject constructor(private val merchantDataDao: MerchantDataDao) : MerchantDataRepository {

    override suspend fun deleteMerchantDataWithId(id: Long) : Int = merchantDataDao.deleteMerchantDataWithId(id)

    override suspend fun deleteMerchantDataWithIdList(idList: List<Long>) = merchantDataDao.deleteMerchantDataWithIdList(idList)

    override suspend fun deleteMerchantDataWithMerchantIdList(idList: List<Long>) = merchantDataDao.deleteMerchantDataWithMerchantIdList(idList)

    override suspend fun deleteMerchantDataWithMerchantId(id: Long) = merchantDataDao.deleteMerchantDataWithMerchantId(id)

    override suspend fun getMerchantDataList(limit: Int, offset: Int) = merchantDataDao.getMerchantDataList(limit, offset)

    override suspend fun getMerchantDataListFromMerchantId(
        id: Long,
        limit: Int,
        offset: Int
    ) = merchantDataDao.getMerchantDataListFromMerchantId(id, limit, offset)

    override fun getMerchantsDataWithMerchantNameList(
        timeZoneOffsetInMilli : Int
    )= merchantDataDao.getMerchantsDataWithMerchantNameList(timeZoneOffsetInMilli)

    override fun searchMerchantDataWithMerchantNameList(
        searchQuery: String,
        timeZoneOffsetInMilli : Int
    ) = merchantDataDao.searchMerchantDataWithMerchantNameList(searchQuery,timeZoneOffsetInMilli)

    override fun getMerchantDataDailyTotalList(
        timeZoneOffsetInMilli: Int
    ) = merchantDataDao.getMerchantDataDailyTotalList(timeZoneOffsetInMilli)


    override suspend fun insert(merchantData: MerchantData) = merchantDataDao.insert(merchantData)

    override suspend fun update(merchantData: MerchantData) = merchantDataDao.update(merchantData)
}