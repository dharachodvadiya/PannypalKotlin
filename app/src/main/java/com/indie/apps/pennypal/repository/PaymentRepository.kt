package com.indie.apps.pennypal.repository

import androidx.paging.PagingSource
import com.indie.apps.pennypal.data.entity.Payment
import kotlinx.coroutines.flow.Flow

interface PaymentRepository : BaseRepository<Payment> {

    suspend fun deleteCustomPayment(paymentId: Long): Int

    suspend fun getPaymentFromId(paymentId: Long): Payment

    fun getPaymentList(): Flow<List<Payment>>

    suspend fun insertPaymentList(payments: List<Payment>): List<Long>

    fun searchPaymentList(searchQuery: String): PagingSource<Int, Payment>
}