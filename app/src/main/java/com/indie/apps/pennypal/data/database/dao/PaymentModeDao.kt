package com.indie.apps.pennypal.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.indie.apps.pennypal.data.database.entity.PaymentMode
import kotlinx.coroutines.flow.Flow

@Dao
interface PaymentModeDao : BaseDao<PaymentMode> {

    @Transaction
    @Query("SELECT * FROM payment_mode")
    fun getPaymentModeList(): Flow<List<PaymentMode>>

    @Insert
    suspend fun insertPaymentModeList(paymentModes: List<PaymentMode>): List<Long>
}