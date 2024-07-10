package com.indie.apps.pannypal.repository

import com.indie.apps.pannypal.data.entity.Merchant
import com.indie.apps.pannypal.data.module.MerchantNameAndDetails

interface MerchantRepository : BaseRepository<Merchant> {

    suspend fun deleteMerchantWithId(id : Long) : Int

    suspend fun deleteMerchantWithIdList(idList: List<Long>) : Int

    suspend fun getMerchantList(limit: Int, offset: Int): List<Merchant>

    suspend fun getMerchantFromId(id : Long) : Merchant

    suspend fun updateAmountWithDate(id: Long, incomeAmt : Long, expenseAmt : Long, dateInMilli: Long ): Int

    suspend fun getMerchantNameAndDetailList(limit: Int, offset: Int): List<MerchantNameAndDetails>

    suspend fun searchMerchantNameAndDetailList(searchQuery : String, limit: Int, offset: Int): List<MerchantNameAndDetails>

    suspend fun searchMerchantList(searchQuery : String, limit: Int, offset: Int): List<Merchant>
}