package com.indie.apps.pannypal.repository

import com.indie.apps.pannypal.data.dao.MerchantDao
import com.indie.apps.pannypal.data.entity.Merchant
import com.indie.apps.pannypal.data.module.MerchantNameAndDetails
import javax.inject.Inject

class MerchantRepositoryImpl @Inject constructor(private val merchantDao: MerchantDao) : MerchantRepository {

    override suspend fun deleteMerchant(merchant: Merchant) = merchantDao.deleteMerchant(merchant)

    override suspend fun deleteMerchants(merchants: List<Merchant>) = merchantDao.deleteMerchants(merchants)

    override suspend fun getMerchants(limit: Int, offset: Int) = merchantDao.getMerchants(limit, offset)

    override suspend fun updateAmountWithDate(
        id: Long,
        incomeAmt: Long,
        expenseAmt: Long,
        dateInMilli: Long
    ) = merchantDao.updateAmountWithDate(id, incomeAmt, expenseAmt, dateInMilli)

    override suspend fun getMerchantsNameAndDetails(
        limit: Int,
        offset: Int
    ) = merchantDao.getMerchantsNameAndDetails(limit, offset)

    override suspend fun searchMerchantsNameAndDetails(
        searchQuery: String,
        limit: Int,
        offset: Int
    ) = merchantDao.searchMerchantsNameAndDetails(searchQuery, limit, offset)

    override suspend fun insert(merchant: Merchant) = merchantDao.insert(merchant)

    override suspend fun update(merchant: Merchant) = merchantDao.update(merchant)
}