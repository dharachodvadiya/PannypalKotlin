package com.indie.apps.pannypal.repository

import androidx.paging.PagingSource
import com.indie.apps.pannypal.data.entity.Merchant
import com.indie.apps.pannypal.data.module.MerchantNameAndDetails

interface MerchantRepository : BaseRepository<Merchant> {

    suspend fun deleteMerchantWithId(id: Long): Int

    suspend fun deleteMerchantWithIdList(idList: List<Long>): Int

    suspend fun getMerchantList(limit: Int, offset: Int): List<Merchant>

    suspend fun getMerchantFromId(id: Long): Merchant

    suspend fun updateAmountWithDate(
        id: Long,
        incomeAmt: Double,
        expenseAmt: Double,
        dateInMilli: Long
    ): Int

    fun searchMerchantNameAndDetailList(searchQuery: String): PagingSource<Int, MerchantNameAndDetails>

    suspend fun searchMerchantList(searchQuery: String, limit: Int, offset: Int): List<Merchant>
}