package com.indie.apps.pannypal.repository

import com.indie.apps.pannypal.data.entity.Payment

interface PaymentRepository : BaseRepository<Payment>{

    suspend fun deleteCustomPayment(paymentId: Long) : Int

    suspend fun getPaymentList(limit: Int, offset: Int): List<Payment>

    suspend fun insertPaymentList(payments: List<Payment>) : List<Long>

    suspend fun searchPaymentList(searchQuery : String, limit: Int, offset: Int): List<Payment>
}