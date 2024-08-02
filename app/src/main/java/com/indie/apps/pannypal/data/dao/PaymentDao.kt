package com.indie.apps.pannypal.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.indie.apps.pannypal.data.entity.Payment
import kotlinx.coroutines.flow.Flow

@Dao
interface PaymentDao : BaseDao<Payment> {

    //insert
    //update
    //delete only custom payment method
    //get data

    //delete only custom payment method
    @Query("DELETE FROM payment_type WHERE id = :paymentId AND pre_added = 0")
    suspend fun deleteCustomPayment(paymentId: Long): Int

    @Transaction
    @Query("SELECT * FROM payment_type")
    fun getPaymentList(): Flow<List<Payment>>

    @Transaction
    @Query("SELECT * FROM payment_type WHERE name LIKE :searchQuery || '%' LIMIT :limit OFFSET :offset")
    suspend fun searchPaymentList(searchQuery: String, limit: Int, offset: Int): List<Payment>

    @Insert
    suspend fun insertPaymentList(payments: List<Payment>): List<Long>
}