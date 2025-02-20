package com.indie.apps.pennypal.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.indie.apps.pennypal.data.database.entity.Payment
import com.indie.apps.pennypal.data.module.PaymentWithMode
import kotlinx.coroutines.flow.Flow

@Dao
interface PaymentDao : BaseDao<Payment> {

    //insert
    //update
    //delete only custom payment method
    //get data

    //delete only custom payment method
    @Query("UPDATE payment_type SET soft_delete = 1 WHERE id = :paymentId")
    suspend fun softDeleteCustomPayment(paymentId: Long): Int

    @Transaction
    @Query("SELECT * FROM payment_type WHERE soft_delete = 0")
    fun getPaymentList(): Flow<List<Payment>>

    @Transaction
    @Query(
        """
        SELECT pt.id as id, 
                pt.name as name, 
                pt.pre_added as preAdded, 
                pm.id as modeId,
                pm.name as modeName
        FROM payment_type pt
        INNER JOIN payment_mode pm ON pt.mode_id = pm.id
        WHERE pt.soft_delete = 0
    """
    )

    fun getPaymentListWithMode(): Flow<List<PaymentWithMode>>

    @Transaction
    @Query("SELECT * FROM payment_type where id = :id")
    suspend fun getPaymentFromId(id: Long): Payment

    @Transaction
    @Query("SELECT * FROM payment_type where name = :name")
    suspend fun getPaymentFromName(name : String): Payment?


    @Transaction
    @Query("SELECT * FROM payment_type WHERE name LIKE :searchQuery || '%' AND soft_delete = 0")
    fun searchPaymentList(searchQuery: String): PagingSource<Int, Payment>

    @Insert
    suspend fun insertPaymentList(payments: List<Payment>): List<Long>
}