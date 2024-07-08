package com.indie.apps.pannypal.repository

import com.indie.apps.pannypal.data.entity.Merchant
import com.indie.apps.pannypal.data.entity.MerchantData

interface MerchantRepository : BaseRepository<Merchant> {

    suspend fun deleteMerchant(merchant: Merchant) : Int

    suspend fun deleteMerchants(merchants: List<Merchant>) : Int

    suspend fun getMerchants(limit: Int, offset: Int): List<Merchant>

    suspend fun updateAmountWithDate(id: Long, incomeAmt : Long, expenseAmt : Long, dateInMilli: Long ): Int
}