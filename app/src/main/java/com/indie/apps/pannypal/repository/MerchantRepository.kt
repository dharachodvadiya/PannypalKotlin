package com.indie.apps.pannypal.repository

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.indie.apps.pannypal.data.entity.Merchant
import com.indie.apps.pannypal.data.module.IncomeAndExpense
import com.indie.apps.pannypal.data.module.MerchantNameAndDetails
import kotlinx.coroutines.flow.Flow

interface MerchantRepository : BaseRepository<Merchant> {

    suspend fun deleteMerchantWithId(id: Long): Int

    suspend fun deleteMerchantWithIdList(idList: List<Long>): Int

    fun getMerchantList(): PagingSource<Int,Merchant>

    suspend fun getMerchantFromId(id: Long): Merchant

    suspend fun getTotalIncomeAndeExpenseFromIds(ids: List<Long>): IncomeAndExpense

    suspend fun updateAmountWithDate(
        id: Long,
        incomeAmt: Double,
        expenseAmt: Double,
        dateInMilli: Long
    ): Int

    fun searchMerchantNameAndDetailList(searchQuery: String): PagingSource<Int, MerchantNameAndDetails>
    fun searchMerchantNameAndDetailListPaging(searchQuery: String): Flow<PagingData<MerchantNameAndDetails>>

    fun searchMerchantList(searchQuery: String): PagingSource<Int, Merchant>
}