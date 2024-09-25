package com.indie.apps.pennypal.repository

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.indie.apps.pennypal.data.database.entity.Merchant
import com.indie.apps.pennypal.data.module.MerchantNameAndDetails
import kotlinx.coroutines.flow.Flow

interface MerchantRepository : BaseRepository<Merchant> {

    suspend fun deleteMerchantWithId(id: Long): Int

    suspend fun deleteMerchantWithIdList(idList: List<Long>): Int

    fun getMerchantList(): PagingSource<Int, Merchant>

    fun getMerchantFromId(id: Long): Flow<Merchant>

    /* suspend fun getTotalIncomeAndeExpenseFromIds(ids: List<Long>): IncomeAndExpense

     suspend fun updateAmountWithDate(
         id: Long,
         incomeAmt: Double,
         expenseAmt: Double
     ): Int

     suspend fun addAmountWithDate(
         id: Long,
         incomeAmt: Double,
         expenseAmt: Double,
         dateInMilli: Long
     ): Int*/

    fun searchMerchantNameAndDetailList(searchQuery: String): PagingSource<Int, MerchantNameAndDetails>

    fun getRecentMerchantNameAndDetailList(): Flow<List<MerchantNameAndDetails>>

    fun searchMerchantNameAndDetailListPaging(searchQuery: String): Flow<PagingData<MerchantNameAndDetails>>

    fun searchMerchantList(searchQuery: String): PagingSource<Int, Merchant>
}