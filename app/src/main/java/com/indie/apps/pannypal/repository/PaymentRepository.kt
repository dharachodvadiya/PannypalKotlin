package com.indie.apps.pannypal.repository

import com.indie.apps.pannypal.data.entity.Payment

interface PaymentRepository : BaseRepository<Payment>{

    suspend fun deleteCustomPayment(paymentId: Long) : Int

    suspend fun getPayments(): List<Payment>

    suspend fun insertPayments(payments: List<Payment>) : List<Long>
}