package com.indie.apps.pennypal.repository

import androidx.paging.PagingSource
import com.indie.apps.pennypal.data.entity.MerchantData
import com.indie.apps.pennypal.data.module.IncomeAndExpense
import com.indie.apps.pennypal.data.module.MerchantDataDailyTotal
import com.indie.apps.pennypal.data.module.MerchantDataWithAllData
import com.indie.apps.pennypal.data.module.MerchantDataWithName
import com.indie.apps.pennypal.data.module.MerchantDataWithNameWithDayTotal
import com.indie.apps.pennypal.data.module.MerchantDataWithPaymentName
import kotlinx.coroutines.flow.Flow

interface MerchantDataRepository : BaseRepository<MerchantData> {
    suspend fun updateMerchantDataPaymentId(oldPaymentId: Long, newPaymentId: Long): Int

    suspend fun deleteMerchantDataWithId(id: Long): Int

    suspend fun deleteMerchantDataWithIdList(idList: List<Long>): Int

    suspend fun deleteMerchantDataWithMerchantIdList(idList: List<Long>): Int

    suspend fun deleteMerchantDataWithMerchantId(id: Long): Int

    suspend fun getTotalIncomeAndeExpenseFromIds(ids: List<Long>): IncomeAndExpense

    suspend fun getMerchantDataFromId(id: Long): MerchantData

    fun getMerchantDataList(): PagingSource<Int, MerchantData>

    fun getMerchantDataListFromMerchantId(merchantId: Long): PagingSource<Int, MerchantData>

    fun searchMerchantsDataWithAllDataList(searchQuery : String): PagingSource<Int, MerchantDataWithAllData>

    fun getRecentMerchantsDataWithAllDataList(): Flow<List<MerchantDataWithAllData>>

    fun getMerchantsDataWithPaymentNameListFromMerchantId(
        merchantId: Long
    ): PagingSource<Int, MerchantDataWithPaymentName>

    fun searchMerchantDataWithMerchantNameList(
        searchQuery: String,
        timeZoneOffsetInMilli: Int
    ): PagingSource<Int, MerchantDataWithName>

    fun getMerchantDataDailyTotalList(timeZoneOffsetInMilli: Int): PagingSource<Int, MerchantDataDailyTotal>

    fun getMerchantDataWithNameWithDayTotal(timeZoneOffsetInMilli: Int): PagingSource<Int, MerchantDataWithNameWithDayTotal>
}