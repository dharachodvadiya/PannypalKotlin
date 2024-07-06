package com.indie.apps.pannypal.repository

import com.indie.apps.pannypal.data.entity.Merchant
import com.indie.apps.pannypal.data.entity.MerchantData

interface MerchantRepository : BaseRepository<Merchant> {

    suspend fun deleteMerchant(merchant: Merchant) : Int

    suspend fun deleteMerchants(vararg merchants: List<Merchant>) : Int

    suspend fun getMerchants(): List<Merchant>
}