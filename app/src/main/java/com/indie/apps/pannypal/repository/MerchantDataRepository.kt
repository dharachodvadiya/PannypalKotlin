package com.indie.apps.pannypal.repository

import com.indie.apps.pannypal.data.entity.MerchantData

interface MerchantDataRepository : BaseRepository<MerchantData>{

    suspend fun deleteMerchantData(merchantData: MerchantData) : Int

    suspend fun deleteMerchantsData(merchantsData: List<MerchantData>) : Int

    suspend fun getMerchantsData(): List<MerchantData>
}