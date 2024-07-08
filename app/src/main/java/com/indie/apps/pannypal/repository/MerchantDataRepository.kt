package com.indie.apps.pannypal.repository

import com.indie.apps.pannypal.data.entity.MerchantData
import com.indie.apps.pannypal.data.module.MerchantDataWithName

interface MerchantDataRepository : BaseRepository<MerchantData>{

    suspend fun deleteMerchantData(merchantData: MerchantData) : Int

    suspend fun deleteMerchantsData(merchantsData: List<MerchantData>) : Int

    suspend fun getMerchantsData(limit: Int, offset: Int): List<MerchantData>

    suspend fun getMerchantsDataWithMerchantName(limit: Int, offset: Int): List<MerchantDataWithName>

    suspend fun searchMerchantDataWithMerchantName(searchQuery : String, limit: Int, offset: Int): List<MerchantDataWithName>


}