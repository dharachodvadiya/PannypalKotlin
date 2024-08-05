package com.indie.apps.pannypal.repository

import androidx.paging.PagingSource
import com.indie.apps.pannypal.data.entity.Payment
import kotlinx.coroutines.flow.Flow

interface PaymentRepository : BaseRepository<Payment> {

    suspend fun deleteCustomPayment(paymentId: Long): Int

    fun getPaymentList(): Flow<List<Payment>>

    suspend fun insertPaymentList(payments: List<Payment>): List<Long>

    fun searchPaymentList(searchQuery: String): PagingSource<Int, Payment>
}