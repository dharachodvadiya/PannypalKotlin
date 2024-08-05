package com.indie.apps.pannypal.repository

import androidx.paging.PagingSource
import com.indie.apps.pannypal.data.entity.MerchantData
import com.indie.apps.pannypal.data.module.IncomeAndExpense
import com.indie.apps.pannypal.data.module.MerchantDataDailyTotal
import com.indie.apps.pannypal.data.module.MerchantDataWithName

interface MerchantDataRepository : BaseRepository<MerchantData> {

    suspend fun deleteMerchantDataWithId(id: Long): Int

    suspend fun deleteMerchantDataWithIdList(idList: List<Long>): Int

    suspend fun deleteMerchantDataWithMerchantIdList(idList: List<Long>): Int

    suspend fun deleteMerchantDataWithMerchantId(id: Long): Int

    suspend fun getTotalIncomeAndeExpenseFromIds(ids: List<Long>): IncomeAndExpense

    suspend fun getMerchantDataFromId(id: Long): MerchantData

    fun getMerchantDataList(): PagingSource<Int, MerchantData>

    fun getMerchantDataListFromMerchantId(merchantId: Long): PagingSource<Int, MerchantData>

    fun getMerchantsDataWithMerchantNameList(timeZoneOffsetInMilli: Int): PagingSource<Int, MerchantDataWithName>

    fun searchMerchantDataWithMerchantNameList(
        searchQuery: String,
        timeZoneOffsetInMilli: Int
    ): PagingSource<Int, MerchantDataWithName>

    fun getMerchantDataDailyTotalList(timeZoneOffsetInMilli: Int): PagingSource<Int, MerchantDataDailyTotal>
}