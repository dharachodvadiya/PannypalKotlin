package com.indie.apps.pannypal.repository

import com.indie.apps.pannypal.data.entity.MerchantData
import com.indie.apps.pannypal.data.module.MerchantDataWithName

interface MerchantDataRepository : BaseRepository<MerchantData>{

    suspend fun deleteMerchantDataWithId(id: Long) : Int

    suspend fun deleteMerchantDataWithIdList(idList: List<Long>) : Int

    suspend fun deleteMerchantDataWithMerchantIdList(idList: List<Long>) : Int

    suspend fun deleteMerchantDataWithMerchantId(id : Long) : Int

    suspend fun getMerchantDataList(limit: Int, offset: Int): List<MerchantData>

    suspend fun getMerchantDataListFromMerchantId(merchantId: Long, limit: Int, offset: Int): List<MerchantData>

    suspend fun getMerchantsDataWithMerchantNameList(limit: Int, offset: Int): List<MerchantDataWithName>

    suspend fun searchMerchantDataWithMerchantNameList(searchQuery : String, limit: Int, offset: Int): List<MerchantDataWithName>


}