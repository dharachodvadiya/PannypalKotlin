package com.indie.apps.pennypal.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.indie.apps.pennypal.data.database.db_entity.Merchant
import com.indie.apps.pennypal.data.module.MerchantNameAndDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface MerchantDao : BaseDao<Merchant> {

    //insert data
    //update data ->  update user income expense amount ....this will also update when merchant data update
    //[delete with id -> all merchant data which contain id] -> update user income expense amount
    //get data

    @Transaction
    @Query("UPDATE merchant SET soft_delete = 1 WHERE id = :id")
    suspend fun softDeleteMerchantWithId(id: Long): Int

    @Transaction
    @Query("UPDATE merchant SET soft_delete = 1 WHERE id IN (:idList)")
    suspend fun softDeleteMerchantWithIdList(idList: List<Long>): Int

    @Transaction
    @Query("SELECT * FROM merchant where name = :name AND soft_delete = 1")
    suspend fun getSoftDeletedMerchantFromName(name: String): Merchant?

    @Transaction
    @Query("SELECT * FROM merchant where id = :id")
    fun getMerchantFromId(id: Long): Flow<Merchant>

    @Transaction
    //@Query("SELECT * FROM merchant ORDER BY date_milli DESC")
    @Query("SELECT * FROM merchant where soft_delete = 0 ORDER BY id DESC")
    fun getMerchantList(): PagingSource<Int, Merchant>

    @Transaction
    //@Query("SELECT id, name, details FROM merchant WHERE name LIKE  '%' || :searchQuery || '%' OR details LIKE  '%' || :searchQuery || '%'ORDER BY date_milli DESC LIMIT :limit OFFSET :offset")
    @Query("SELECT id, name, details FROM merchant WHERE (name LIKE  '%' || :searchQuery || '%' OR details LIKE  '%' || :searchQuery || '%') AND soft_delete = 0 ORDER BY id DESC LIMIT :limit OFFSET :offset ")
    suspend fun searchMerchantNameAndDetailList(
        searchQuery: String,
        limit: Int,
        offset: Int
    ): List<MerchantNameAndDetails>

    @Transaction
    //@Query("SELECT id, name, details FROM merchant WHERE name LIKE  '%' || :searchQuery || '%' OR details LIKE  '%' || :searchQuery || '%'ORDER BY date_milli DESC")
    @Query("SELECT id, name, details FROM merchant WHERE (name LIKE  '%' || :searchQuery || '%' OR details LIKE  '%' || :searchQuery || '%') AND soft_delete = 0 ORDER BY id DESC")
    fun searchMerchantNameAndDetailList(searchQuery: String): PagingSource<Int, MerchantNameAndDetails>

    @Transaction
    //@Query("SELECT id, name, details FROM merchant WHERE name LIKE  '%' || :searchQuery || '%' OR details LIKE  '%' || :searchQuery || '%'ORDER BY date_milli DESC")
    @Query("SELECT id, name, details FROM merchant where soft_delete = 0 ORDER BY id DESC LIMIT 4 ")
    fun getRecentMerchantNameAndDetailList(): Flow<List<MerchantNameAndDetails>>

    @Transaction
    //@Query("SELECT * FROM merchant WHERE name LIKE  '%' || :searchQuery || '%' OR details LIKE  '%' || :searchQuery || '%' ORDER BY date_milli DESC")
    @Query("SELECT * FROM merchant WHERE (name LIKE  '%' || :searchQuery || '%' OR details LIKE  '%' || :searchQuery || '%') AND soft_delete = 0 ORDER BY id DESC")
    fun searchMerchantList(searchQuery: String): PagingSource<Int, Merchant>


    /*@Transaction
    @Query(
        "UPDATE merchant " +
                "SET income_amt = income_amt + :incomeAmt, " +
                "expense_amt = expense_amt + :expenseAmt " +
                "WHERE ID = :id"
    )
    suspend fun updateAmountWithDate(
        id: Long,
        incomeAmt: Double,
        expenseAmt: Double
    ): Int*/

    /*@Transaction
    @Query(
        "UPDATE merchant " +
                "SET income_amt = income_amt + :incomeAmt, " +
                "expense_amt = expense_amt + :expenseAmt, " +
                "date_milli = :dateInMilli " +
                "WHERE ID = :id"
    )
    suspend fun addAmountWithDate(
        id: Long,
        incomeAmt: Double,
        expenseAmt: Double,
        dateInMilli: Long
    ): Int*/

    /*@Transaction
    @Query(
        """
        SELECT
            SUM(income_amt) as totalIncome,
            SUM(expense_amt) as totalExpense
        FROM merchant
        WHERE ID IN (:ids)
    """
    )
    suspend fun getTotalIncomeAndeExpenseFromIds(ids: List<Long>): IncomeAndExpense*/

}