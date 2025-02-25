package com.indie.apps.pennypal.repository

import androidx.paging.PagingSource
import com.indie.apps.pennypal.data.database.entity.MerchantData
import com.indie.apps.pennypal.data.module.IncomeAndExpense
import com.indie.apps.pennypal.data.module.MerchantDataWithAllData
import com.indie.apps.pennypal.data.module.MerchantDataWithName
import com.indie.apps.pennypal.data.module.MerchantDataWithNameWithDayTotal
import com.indie.apps.pennypal.data.module.MerchantDataWithPaymentName
import com.indie.apps.pennypal.data.module.balance.TotalAllTime
import com.indie.apps.pennypal.data.module.balance.TotalMonthly
import com.indie.apps.pennypal.data.module.balance.TotalYearly
import com.indie.apps.pennypal.data.module.category.CategoryAllTime
import com.indie.apps.pennypal.data.module.category.CategoryAmount
import com.indie.apps.pennypal.data.module.category.CategoryMonthly
import com.indie.apps.pennypal.data.module.category.CategoryYearly
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

    fun searchMerchantsDataWithAllDataList(searchQuery: String): PagingSource<Int, MerchantDataWithAllData>

    fun getRecentMerchantsDataWithAllDataList(): Flow<List<MerchantDataWithAllData>>

    fun getRecentMerchantsDataWithAllDataListFromMonth(
        timeZoneOffsetInMilli: Int,
        monthOffset: Int
    ): Flow<List<MerchantDataWithAllData>>

    fun getRecentMerchantsDataWithAllDataListFromYear(
        timeZoneOffsetInMilli: Int,
        yearOffset: Int
    ): Flow<List<MerchantDataWithAllData>>

    fun getMerchantsDataWithPaymentNameListFromMerchantId(
        merchantId: Long
    ): PagingSource<Int, MerchantDataWithPaymentName>

    fun searchMerchantDataWithMerchantNameList(
        searchQuery: String,
        timeZoneOffsetInMilli: Int
    ): PagingSource<Int, MerchantDataWithName>

    fun getMerchantDataWithNameWithDayTotal(timeZoneOffsetInMilli: Int): PagingSource<Int, MerchantDataWithNameWithDayTotal>

    fun getTotalFromMonth(timeZoneOffsetInMilli: Int, monthOffset: Int): Flow<TotalMonthly?>

    fun getTotalFromYear(timeZoneOffsetInMilli: Int, offset: Int): Flow<TotalYearly?>

    fun getTotal(): Flow<TotalAllTime?>

    fun getCategoryWiseExpenseFromMonthAsFlow(
        timeZoneOffsetInMilli: Int,
        year: Int,
        monthPlusOne: Int,
    ): Flow<List<CategoryMonthly>>

    suspend fun getCategoryWiseExpenseFromMonth(
        timeZoneOffsetInMilli: Int,
        year: Int,
        monthPlusOne: Int,
    ): List<CategoryMonthly>

    fun getCategoryWiseExpenseAsFlow(
    ): Flow<List<CategoryAllTime>>

    suspend fun getCategoryWiseExpense(
    ): List<CategoryAllTime>

    fun getCategoryWiseExpenseFromYearAsFlow(
        timeZoneOffsetInMilli: Int,
        year: Int
    ): Flow<List<CategoryYearly>>

    suspend fun getCategoryWiseExpenseFromYear(
        timeZoneOffsetInMilli: Int,
        year: Int
    ): List<CategoryYearly>

    suspend fun getTotalAmountForMonthAndCategory(
        timeZoneOffsetInMilli: Int,
        year: Int,
        monthPlusOne: Int,
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
        monthPlusOne: Int,
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