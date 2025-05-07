package com.indie.apps.pennypal.repository

import com.indie.apps.pennypal.data.database.db_entity.PaymentMode
import kotlinx.coroutines.flow.Flow

interface PaymentModeRepository : BaseRepository<PaymentMode> {

    fun getPaymentModeList(): Flow<List<PaymentMode>>

    suspend fun insertPaymentModeList(paymentModes: List<PaymentMode>): List<Long>
}