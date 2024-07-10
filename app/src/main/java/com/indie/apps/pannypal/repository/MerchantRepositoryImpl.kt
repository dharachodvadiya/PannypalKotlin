package com.indie.apps.pannypal.repository

import com.indie.apps.pannypal.data.dao.MerchantDao
import com.indie.apps.pannypal.data.entity.Merchant
import javax.inject.Inject

class MerchantRepositoryImpl @Inject constructor(private val merchantDao: MerchantDao) : MerchantRepository {

    override suspend fun deleteMerchantWithId(id : Long) = merchantDao.deleteMerchantWithId(id)

    override suspend fun deleteMerchantWithIdList(idList: List<Long>) = merchantDao.deleteMerchantWithIdList(idList)

    override suspend fun getMerchantList(limit: Int, offset: Int) = merchantDao.getMerchantList(limit, offset)

    override suspend fun getMerchantFromId(id: Long) = merchantDao.getMerchantFromId(id)

    override suspend fun updateAmountWithDate(
        id: Long,
        incomeAmt: Long,
        expenseAmt: Long,
        dateInMilli: Long
    ) = merchantDao.updateAmountWithDate(id, incomeAmt, expenseAmt, dateInMilli)

    override suspend fun getMerchantNameAndDetailList(
        limit: Int,
        offset: Int
    ) = merchantDao.getMerchantNameAndDetailList(limit, offset)

    override suspend fun searchMerchantNameAndDetailList(
        searchQuery: String,
        limit: Int,
        offset: Int
    ) = merchantDao.searchMerchantNameAndDetailList(searchQuery, limit, offset)

    override suspend fun searchMerchantList(
        searchQuery: String,
        limit: Int,
        offset: Int
    ) = merchantDao.searchMerchantList(searchQuery, limit, offset)

    override suspend fun insert(merchant: Merchant) = merchantDao.insert(merchant)

    override suspend fun update(merchant: Merchant) = merchantDao.update(merchant)
}