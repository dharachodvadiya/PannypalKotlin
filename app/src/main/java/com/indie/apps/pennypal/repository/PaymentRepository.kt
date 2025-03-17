package com.indie.apps.pennypal.repository

import androidx.paging.PagingSource
import com.indie.apps.pennypal.data.database.entity.Payment
import com.indie.apps.pennypal.data.module.payment.PaymentWithMode
import kotlinx.coroutines.flow.Flow

interface PaymentRepository : BaseRepository<Payment> {

    suspend fun deleteCustomPayment(paymentId: Long): Int

    suspend fun getPaymentFromId(paymentId: Long): Payment

    fun getPaymentList(): Flow<List<Payment>>

    fun getPaymentListWithMode(): Flow<List<PaymentWithMode>>

    suspend fun insertPaymentList(payments: List<Payment>): List<Long>

    fun searchPaymentList(searchQuery: String): PagingSource<Int, Payment>
}