package com.indie.apps.pennypal.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.indie.apps.pennypal.data.entity.Payment
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
    @Query("SELECT * FROM payment_type where id = :id")
    suspend fun getPaymentFromId(id: Long): Payment

    @Transaction
    @Query("SELECT * FROM payment_type WHERE name LIKE :searchQuery || '%'")
    fun searchPaymentList(searchQuery: String): PagingSource<Int, Payment>

    @Insert
    suspend fun insertPaymentList(payments: List<Payment>): List<Long>
}