package com.indie.apps.pannypal.repository

import com.indie.apps.pannypal.data.entity.Merchant
import com.indie.apps.pannypal.data.entity.MerchantData
import com.indie.apps.pannypal.data.module.MerchantNameAndDetails

interface MerchantRepository : BaseRepository<Merchant> {

    suspend fun deleteMerchant(merchant: Merchant) : Int

    suspend fun deleteMerchants(merchants: List<Merchant>) : Int

    suspend fun getMerchants(limit: Int, offset: Int): List<Merchant>

    suspend fun updateAmountWithDate(id: Long, incomeAmt : Long, expenseAmt : Long, dateInMilli: Long ): Int

    suspend fun getMerchantsNameAndDetails(limit: Int, offset: Int): List<MerchantNameAndDetails>

    suspend fun searchMerchantsNameAndDetails(searchQuery : String, limit: Int, offset: Int): List<MerchantNameAndDetails>

    suspend fun searchMerchants(searchQuery : String, limit: Int, offset: Int): List<Merchant>
}