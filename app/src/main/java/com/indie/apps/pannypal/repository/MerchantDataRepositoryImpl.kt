package com.indie.apps.pannypal.repository

import com.indie.apps.pannypal.data.dao.MerchantDataDao
import com.indie.apps.pannypal.data.entity.MerchantData
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

    override suspend fun getMerchantsDataWithMerchantNameList(
        limit: Int,
        offset: Int
    ): List<MerchantDataWithName>  = merchantDataDao.getMerchantsDataWithMerchantNameList(limit, offset)

    override suspend fun searchMerchantDataWithMerchantNameList(
        searchQuery: String,
        limit: Int,
        offset: Int
    ) = merchantDataDao.searchMerchantDataWithMerchantNameList(searchQuery, limit, offset)

    override suspend fun insert(merchantData: MerchantData) = merchantDataDao.insert(merchantData)

    override suspend fun update(merchantData: MerchantData) = merchantDataDao.update(merchantData)
}