package com.indie.apps.pennypal.repository

import androidx.paging.PagingSource
import com.indie.apps.pennypal.data.database.entity.MerchantData
import com.indie.apps.pennypal.data.module.balance.Total
import com.indie.apps.pennypal.data.module.category.CategoryAmount
import com.indie.apps.pennypal.data.module.merchant_data.MerchantDataWithAllData
import com.indie.apps.pennypal.data.module.merchant_data.MerchantDataWithName
import com.indie.apps.pennypal.data.module.merchant_data.MerchantDataWithPaymentName
import kotlinx.coroutines.flow.Flow

interface MerchantDataRepository : BaseRepository<MerchantData> {
    suspend fun updateMerchantDataPaymentId(oldPaymentId: Long, newPaymentId: Long): Int

    suspend fun deleteMerchantDataWithId(id: Long): Int

    suspend fun deleteMerchantDataWithIdList(idList: List<Long>): Int

    suspend fun deleteMerchantDataWithMerchantIdList(idList: List<Long>): Int

    suspend fun deleteMerchantDataWithMerchantId(id: Long): Int

    //suspend fun getTotalIncomeAndeExpenseFromIds(ids: List<Long>): IncomeAndExpense

    suspend fun getMerchantDataFromId(id: Long): MerchantData

    fun getMerchantDataList(): PagingSource<Int, MerchantData>

    fun getMerchantDataListFromMerchantId(merchantId: Long): PagingSource<Int, MerchantData>

    fun searchMerchantsDataWithAllDataList(searchQuery: String): PagingSource<Int, MerchantDataWithAllData>

    fun getRecentMerchantsDataWithAllDataList(): Flow<List<MerchantDataWithAllData>>

    fun getRecentMerchantsDataWithAllDataListFromMonth(
        timeZoneOffsetInMilli: Int,
        month: Int
    ): Flow<List<MerchantDataWithAllData>>

    fun getRecentMerchantsDataWithAllDataListFromYear(
        timeZoneOffsetInMilli: Int,
        year: Int
    ): Flow<List<MerchantDataWithAllData>>

    fun getMerchantsDataWithPaymentNameListFromMerchantId(
        merchantId: Long
    ): PagingSource<Int, MerchantDataWithPaymentName>

    fun searchMerchantDataWithMerchantNameList(
        searchQuery: String,
        timeZoneOffsetInMilli: Int
    ): PagingSource<Int, MerchantDataWithName>

    // fun getMerchantDataWithNameWithDayTotal(timeZoneOffsetInMilli: Int): PagingSource<Int, MerchantDataWithNameWithDayTotal>

    fun getTotalFromMonthAsFlow(
        timeZoneOffsetInMilli: Int,
        year: Int,
        month: Int
    ): Flow<Total>

    suspend fun getTotalFromMonth(
        timeZoneOffsetInMilli: Int,
        year: Int,
        month: Int
    ): Total

    fun getTotalFromYearAsFlow(timeZoneOffsetInMilli: Int, year: Int): Flow<Total>

    suspend fun getTotalFromYear(timeZoneOffsetInMilli: Int, year: Int): Total

    fun getTotalAsFlow(): Flow<Total>

    suspend fun getTotal(): Total

    fun getCategoryWiseExpenseFromMonthAsFlow(
        timeZoneOffsetInMilli: Int,
        year: Int,
        month: Int,
    ): Flow<List<CategoryAmount>>

    suspend fun getCategoryWiseExpenseFromMonth(
        timeZoneOffsetInMilli: Int,
        year: Int,
        month: Int,
    ): List<CategoryAmount>

    fun getCategoryWiseExpenseAsFlow(
    ): Flow<List<CategoryAmount>>

    suspend fun getCategoryWiseExpense(
    ): List<CategoryAmount>

    fun getCategoryWiseExpenseFromYearAsFlow(
        timeZoneOffsetInMilli: Int,
        year: Int
    ): Flow<List<CategoryAmount>>

    suspend fun getCategoryWiseExpenseFromYear(
        timeZoneOffsetInMilli: Int,
        year: Int
    ): List<CategoryAmount>

    suspend fun getTotalAmountForMonthAndCategory(
        timeZoneOffsetInMilli: Int,
        year: Int,
        month: Int,
        categoryIds: List<Long>
    ): Double

    suspend fun getTotalAmountForYearAndCategory(
        timeZoneOffsetInMilli: Int,
        year: Int,
        categoryIds: List<Long>
    ): Double

    suspend fun getTotalAmountForBetweenDatesAndCategory(
        timeZoneOffsetInMilli: Int,
        startTime: Long,
        endTime: Long,
        categoryIds: List<Long>
    ): Double

    fun getCategoryWiseTotalAmountForMonth(
        timeZoneOffsetInMilli: Int,
        year: Int,
        month: Int,
        categoryIds: List<Long>
    ): Flow<List<CategoryAmount>>

    fun getCategoryWiseTotalAmountForYear(
        timeZoneOffsetInMilli: Int,
        year: Int,
        categoryIds: List<Long>
    ): Flow<List<CategoryAmount>>

    fun getCategoryWiseTotalAmountForBetweenDates(
        timeZoneOffsetInMilli: Int,
        startTime: Long,
        endTime: Long,
        categoryIds: List<Long>
    ): Flow<List<CategoryAmount>>
}