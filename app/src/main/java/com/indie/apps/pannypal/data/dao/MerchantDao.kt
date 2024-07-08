package com.indie.apps.pannypal.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.indie.apps.pannypal.data.entity.Merchant
import com.indie.apps.pannypal.data.entity.MerchantData
import com.indie.apps.pannypal.data.entity.Payment
import com.indie.apps.pannypal.data.entity.User

@Dao
interface MerchantDao : BaseDao<Merchant> {

    //insert data
    //update data ->  update user income expense amount ....this will also update when merchant data update
    //[delete with id -> all merchant data which contain id] -> update user income expense amount
    //get data

    @Delete
    suspend fun deleteMerchant(merchant: Merchant) : Int

    @Delete
    suspend fun deleteMerchants(merchants: List<Merchant>) : Int

   /* @Transaction
    @Query("SELECT * FROM merchant")
    fun getMerchants(): List<Merchant>
*/
    @Transaction
    @Query("SELECT * FROM merchant ORDER BY id DESC LIMIT :limit OFFSET :offset")
    suspend fun getMerchants(limit: Int, offset: Int): List<Merchant>
}