package com.indie.apps.pannypal.repository

import com.indie.apps.pannypal.data.entity.Payment

interface PaymentRepository : BaseRepository<Payment>{

    suspend fun deleteCustomPayment(paymentId: Long) : Int

    suspend fun getPayments(limit: Int, offset: Int): List<Payment>

    suspend fun insertPayments(payments: List<Payment>) : List<Long>

    suspend fun searchPayments(searchQuery : String, limit: Int, offset: Int): List<Payment>
}