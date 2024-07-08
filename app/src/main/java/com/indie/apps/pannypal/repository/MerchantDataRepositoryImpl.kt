package com.indie.apps.pannypal.repository

import com.indie.apps.pannypal.data.dao.MerchantDataDao
import com.indie.apps.pannypal.data.entity.MerchantData
import com.indie.apps.pannypal.data.module.MerchantDataWithName
import javax.inject.Inject

class MerchantDataRepositoryImpl @Inject constructor(private val merchantDataDao: MerchantDataDao) : MerchantDataRepository {

    override suspend fun deleteMerchantData(merchantData: MerchantData) = merchantDataDao.deleteMerchantData(merchantData)

    override suspend fun deleteMerchantsData(merchantsData: List<MerchantData>) = merchantDataDao.deleteMerchantsData(merchantsData)

    override suspend fun getMerchantsData(limit: Int, offset: Int) = merchantDataDao.getMerchantsData(limit, offset)

    override suspend fun getMerchantsDataWithMerchantName(
        limit: Int,
        offset: Int
    ): List<MerchantDataWithName>  = merchantDataDao.getMerchantsDataWithMerchantName(limit, offset)

    override suspend fun insert(merchantData: MerchantData) = merchantDataDao.insert(merchantData)

    override suspend fun update(merchantData: MerchantData) = merchantDataDao.update(merchantData)
}