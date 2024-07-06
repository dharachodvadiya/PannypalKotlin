package com.indie.apps.pannypal.repository

import com.indie.apps.pannypal.data.dao.MerchantDao
import com.indie.apps.pannypal.data.entity.Merchant

class MerchantRepositoryImpl(private val merchantDao: MerchantDao) : MerchantRepository {

    override suspend fun deleteMerchant(merchant: Merchant) = merchantDao.deleteMerchant(merchant)

    override suspend fun deleteMerchants(vararg merchants: List<Merchant>) = merchantDao.deleteMerchants(*merchants)

    override suspend fun getMerchants() = merchantDao.getMerchants()

    override suspend fun insert(merchant: Merchant) = merchantDao.insert(merchant)

    override suspend fun update(merchant: Merchant) = merchantDao.update(merchant)
}