package com.indie.apps.pannypal.repository

import com.indie.apps.pannypal.data.dao.MerchantDataDao
import com.indie.apps.pannypal.data.entity.MerchantData
import javax.inject.Inject

class MerchantDataRepositoryImpl @Inject constructor(private val merchantDataDao: MerchantDataDao) : MerchantDataRepository {

    override suspend fun deleteMerchantData(merchantData: MerchantData) = merchantDataDao.deleteMerchantData(merchantData)

    override suspend fun deleteMerchantsData(merchantsData: List<MerchantData>) = merchantDataDao.deleteMerchantsData(merchantsData)

    override suspend fun getMerchantsData() = merchantDataDao.getMerchantsData()

    override suspend fun insert(merchantData: MerchantData) = merchantDataDao.insert(merchantData)

    override suspend fun update(merchantData: MerchantData) = merchantDataDao.update(merchantData)
}