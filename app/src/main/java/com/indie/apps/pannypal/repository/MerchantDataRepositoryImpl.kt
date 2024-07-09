package com.indie.apps.pannypal.repository

import com.indie.apps.pannypal.data.dao.MerchantDataDao
import com.indie.apps.pannypal.data.entity.MerchantData
import com.indie.apps.pannypal.data.module.MerchantDataWithName
import javax.inject.Inject

class MerchantDataRepositoryImpl @Inject constructor(private val merchantDataDao: MerchantDataDao) : MerchantDataRepository {

    override suspend fun deleteMerchantDataWithId(id: Long) : Int = merchantDataDao.deleteMerchantDataWithId(id)

    override suspend fun deleteMerchantsWithId(idList: List<Long>) = merchantDataDao.deleteMerchantsWithId(idList)

    override suspend fun deleteMerchantsWithMerchantIds(idList: List<Long>) = merchantDataDao.deleteMerchantsWithMerchantIds(idList)

    override suspend fun deleteMerchantsWithMerchantId(id: Long) = merchantDataDao.deleteMerchantsWithMerchantId(id)

    //override suspend fun deleteMerchantsData(merchantsData: List<MerchantData>) = merchantDataDao.deleteMerchantsData(merchantsData)

    override suspend fun getMerchantsData(limit: Int, offset: Int) = merchantDataDao.getMerchantsData(limit, offset)

    override suspend fun getMerchantsDataWithMerchantName(
        limit: Int,
        offset: Int
    ): List<MerchantDataWithName>  = merchantDataDao.getMerchantsDataWithMerchantName(limit, offset)

    override suspend fun searchMerchantDataWithMerchantName(
        searchQuery: String,
        limit: Int,
        offset: Int
    ) = merchantDataDao.searchMerchantDataWithMerchantName(searchQuery, limit, offset)

    override suspend fun insert(merchantData: MerchantData) = merchantDataDao.insert(merchantData)

    override suspend fun update(merchantData: MerchantData) = merchantDataDao.update(merchantData)
}