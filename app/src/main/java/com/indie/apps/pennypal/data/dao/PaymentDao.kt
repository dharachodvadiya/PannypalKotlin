package com.indie.apps.pennypal.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.indie.apps.pennypal.data.entity.Payment
import com.indie.apps.pennypal.data.module.MerchantDataWithPaymentName
import com.indie.apps.pennypal.data.module.MerchantNameAndDetails
import com.indie.apps.pennypal.data.module.PaymentWithMode
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
    @Query(
        """
        SELECT pt.id as id, 
                pt.name as name, 
                pt.pre_added as preAdded, 
                pm.id as modeId,
                pm.name as modeName
        FROM payment_type pt
        INNER JOIN payment_mode pm ON pt.mode_id = pm.id
    """
    )

    fun getPaymentListWithMode(): Flow<List<PaymentWithMode>>

    @Transaction
    @Query("SELECT * FROM payment_type where id = :id")
    suspend fun getPaymentFromId(id: Long): Payment

    @Transaction
    @Query("SELECT * FROM payment_type WHERE name LIKE :searchQuery || '%'")
    fun searchPaymentList(searchQuery: String): PagingSource<Int, Payment>

    @Insert
    suspend fun insertPaymentList(payments: List<Payment>): List<Long>
}