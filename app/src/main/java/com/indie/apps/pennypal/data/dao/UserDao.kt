package com.indie.apps.pennypal.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.indie.apps.pennypal.data.entity.User
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

    @Transaction
    @Query("UPDATE user SET income_amt = income_amt + :incomeAmt, expense_amt = expense_amt + :expenseAmt WHERE ID = 1")
    suspend fun updateAmount(incomeAmt: Double, expenseAmt: Double): Int
}