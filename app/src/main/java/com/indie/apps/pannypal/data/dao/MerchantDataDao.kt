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
import com.indie.apps.pannypal.data.module.MerchantDataWithName
import kotlinx.coroutines.flow.Flow

@Dao
interface MerchantDataDao : BaseDao<MerchantData> {

    //insert data -> update merchant income-expense
    //update data -> update merchant income-expense
    //delete data -> update merchant income-expense
    //get data

    @Transaction
    @Query("delete from merchant_data where id = :id")
    suspend fun deleteMerchantDataWithId(id: Long) : Int

    @Transaction
    @Query("delete from merchant_data where id In (:idList)")
    suspend fun deleteMerchantsWithId(idList: List<Long>) : Int

    @Transaction
    @Query("delete from merchant_data where merchant_id In (:idList)")
    suspend fun deleteMerchantsWithMerchantIds(idList: List<Long>) : Int

    @Transaction
    @Query("delete from merchant_data where merchant_id = :id")
    suspend fun deleteMerchantsWithMerchantId(id: Long) : Int

    @Transaction
    @Query("SELECT * FROM merchant_data ORDER BY id DESC LIMIT :limit OFFSET :offset")
    suspend fun getMerchantsData(limit: Int, offset: Int): List<MerchantData>

    @Transaction
    @Query("""
        SELECT md.id as id, 
                md.merchant_id as merchantId, 
                md.date_milli as dateInMilli, 
                md.details, 
                md.amount, 
                m.name as merchantName
        FROM merchant_data md
        INNER JOIN merchant m ON md.merchant_id = m.id
        ORDER BY id DESC LIMIT :limit OFFSET :offset
    """)
    suspend fun getMerchantsDataWithMerchantName(limit: Int, offset: Int): List<MerchantDataWithName>

    @Transaction
    @Query("""
        SELECT md.id as id, 
                md.merchant_id as merchantId, 
                md.date_milli as dateInMilli, 
                md.details, 
                md.amount, 
                m.name as merchantName
        FROM merchant_data md
        INNER JOIN merchant m ON md.merchant_id = m.id
        WHERE m.name LIKE :searchQuery || '%' OR md.details LIKE :searchQuery || '%'
        ORDER BY id DESC LIMIT :limit OFFSET :offset
    """)
    suspend fun searchMerchantDataWithMerchantName(searchQuery : String, limit: Int, offset: Int): List<MerchantDataWithName>
}