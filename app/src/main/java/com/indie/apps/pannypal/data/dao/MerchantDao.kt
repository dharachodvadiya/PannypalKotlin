package com.indie.apps.pannypal.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.indie.apps.pannypal.data.entity.Merchant
import com.indie.apps.pannypal.data.module.MerchantNameAndDetails

@Dao
interface MerchantDao : BaseDao<Merchant> {

    //insert data
    //update data ->  update user income expense amount ....this will also update when merchant data update
    //[delete with id -> all merchant data which contain id] -> update user income expense amount
    //get data

    @Transaction
    @Query("delete from merchant where id = :id")
    suspend fun deleteMerchantWithId(id : Long) : Int

    @Transaction
    @Query("delete from merchant where id IN (:idList)")
    suspend fun deleteMerchantsWithId(idList: List<Long>) : Int

    @Transaction
    @Query("SELECT * FROM merchant where id = :id")
    suspend fun getMerchantFromId(id : Long) : Merchant

    @Transaction
    @Query("SELECT * FROM merchant ORDER BY date_milli DESC LIMIT :limit OFFSET :offset")
    suspend fun getMerchants(limit: Int, offset: Int): List<Merchant>

    @Transaction
    @Query("SELECT id, name, details FROM merchant ORDER BY date_milli DESC LIMIT :limit OFFSET :offset")
    suspend fun getMerchantsNameAndDetails(limit: Int, offset: Int): List<MerchantNameAndDetails>

    @Transaction
    @Query("SELECT id, name, details FROM merchant WHERE name LIKE :searchQuery || '%' OR details LIKE :searchQuery || '%'ORDER BY date_milli DESC LIMIT :limit OFFSET :offset")
    suspend fun searchMerchantsNameAndDetails(searchQuery : String, limit: Int, offset: Int): List<MerchantNameAndDetails>

    @Transaction
    @Query("SELECT * FROM merchant WHERE name LIKE :searchQuery || '%' OR details LIKE :searchQuery || '%' ORDER BY date_milli DESC LIMIT :limit OFFSET :offset")
    suspend fun searchMerchants(searchQuery : String, limit: Int, offset: Int): List<Merchant>


    @Transaction
    @Query("UPDATE merchant " +
            "SET income_amt = income_amt + :incomeAmt, " +
            "expense_amt = expense_amt + :expenseAmt, " +
            "date_milli = :dateInMilli " +
            "WHERE ID = :id")
    suspend fun updateAmountWithDate(id: Long, incomeAmt : Long, expenseAmt : Long, dateInMilli: Long ): Int
}