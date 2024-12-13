package com.indie.apps.pennypal.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.indie.apps.pennypal.data.database.entity.User
import com.indie.apps.pennypal.data.module.UserWithPaymentName
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao : BaseDao<User> {

    //insert user data
    //update data.....this will also update when merchant update [merchant -> user]
    //get user data
    //can not delete

    //this always has single user
    @Query("SELECT * FROM user WHERE id = 1")
    fun getUser(): Flow<User>

    @Query(
        """
        SELECT u.name as name, 
                u.email as email,
                u.currency as currency,
                u.country_code as currencyCountryCode,
                u.payment_id as paymentId,
                p.name as paymentName
        FROM user u
        INNER JOIN payment_type p ON u.payment_id = p.id
        WHERE u.id = 1
    """
    )
    fun getUserWithPaymentName(): Flow<UserWithPaymentName>

    @Transaction
    @Query("UPDATE user SET payment_id = :paymentId WHERE ID = 1")
    suspend fun updatePayment(paymentId: Long): Int

    @Transaction
    @Query("UPDATE user SET currency = :currency, country_code = :countryCode WHERE ID = 1")
    suspend fun updateCurrency(currency: String, countryCode: String): Int

    @Transaction
    @Query("UPDATE user SET name = :name WHERE ID = 1")
    suspend fun updateName(name: String): Int

    // @Transaction
    // @Query("UPDATE user SET income_amt = income_amt + :incomeAmt, expense_amt = expense_amt + :expenseAmt WHERE ID = 1")
    // suspend fun updateAmount(incomeAmt: Double, expenseAmt: Double): Int

    @Transaction
    @Query("UPDATE user SET last_sync_date_milli = :lastSyncDateInMilli WHERE ID = 1")
    suspend fun updateLastSyncTime(lastSyncDateInMilli: Long): Int

    @Transaction
    @Query("UPDATE user SET payment_id = 1 WHERE ID = 1")
    suspend fun updateWithDefaultPayment(): Int
}