package com.indie.apps.pannypal.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.indie.apps.pannypal.data.entity.Payment
import com.indie.apps.pannypal.data.entity.User

@Dao
interface PaymentDao : BaseDao<Payment> {

    //insert
    //update
    //delete only custom payment method
    //get data

    //delete only custom payment method
    @Query("DELETE FROM payment_type WHERE id = :paymentId AND pre_added = 0")
    suspend fun deleteCustomPayment(paymentId: Long) : Int

    @Transaction
    @Query("SELECT * FROM payment_type")
    suspend fun getPayments(): List<Payment>

    @Insert
    suspend fun insertPayments(payments: List<Payment>) : List<Long>
}