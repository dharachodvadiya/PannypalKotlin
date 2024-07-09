package com.indie.apps.pannypal.repository

import com.indie.apps.pannypal.data.entity.MerchantData
import com.indie.apps.pannypal.data.module.MerchantDataWithName

interface MerchantDataRepository : BaseRepository<MerchantData>{

    suspend fun deleteMerchantDataWithId(id: Long) : Int

    suspend fun deleteMerchantsWithId(idList: List<Long>) : Int

    suspend fun deleteMerchantsWithMerchantIds(idList: List<Long>) : Int

    suspend fun deleteMerchantsWithMerchantId(id : Long) : Int

    suspend fun getMerchantsData(limit: Int, offset: Int): List<MerchantData>

    suspend fun getMerchantsDataWithMerchantName(limit: Int, offset: Int): List<MerchantDataWithName>

    suspend fun searchMerchantDataWithMerchantName(searchQuery : String, limit: Int, offset: Int): List<MerchantDataWithName>


}