package com.indie.apps.pannypal.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.indie.apps.pannypal.data.entity.MerchantData
import com.indie.apps.pannypal.data.module.MerchantDataDailyTotal
import com.indie.apps.pannypal.data.module.MerchantDataWithName
import com.indie.apps.pannypal.util.Constant

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
    suspend fun deleteMerchantDataWithIdList(idList: List<Long>) : Int

    @Transaction
    @Query("delete from merchant_data where merchant_id In (:idList)")
    suspend fun deleteMerchantDataWithMerchantIdList(idList: List<Long>) : Int

    @Transaction
    @Query("delete from merchant_data where merchant_id = :id")
    suspend fun deleteMerchantDataWithMerchantId(id: Long) : Int

    @Transaction
    @Query("SELECT * FROM merchant_data ORDER BY id DESC LIMIT :limit OFFSET :offset")
    suspend fun getMerchantDataList(limit: Int, offset: Int): List<MerchantData>

    @Transaction
    @Query("SELECT * FROM merchant_data where merchant_id = :merchantId ORDER BY id DESC LIMIT :limit OFFSET :offset")
    suspend fun getMerchantDataListFromMerchantId(merchantId: Long, limit: Int, offset: Int): List<MerchantData>

    @Transaction
    @Query("""
        SELECT md.id as id, 
                md.merchant_id as merchantId, 
                md.date_milli as dateInMilli, 
                strftime('%d-%m-%Y', (md.date_milli + :timeZoneOffsetInMilli) / 1000, 'unixepoch') as day,
                md.details, 
                md.amount, 
                md.type,
                m.name as merchantName
        FROM merchant_data md
        INNER JOIN merchant m ON md.merchant_id = m.id
        ORDER BY id DESC
    """)

    fun getMerchantsDataWithMerchantNameList(timeZoneOffsetInMilli : Int): PagingSource<Int,MerchantDataWithName>

    @Transaction
    @Query("""
        SELECT md.id as id, 
                md.merchant_id as merchantId, 
                md.date_milli as dateInMilli, 
                strftime('%d-%m-%Y', (md.date_milli + :timeZoneOffsetInMilli) / 1000, 'unixepoch') as day,
                md.details, 
                md.amount, 
                md.type,
                m.name as merchantName
        FROM merchant_data md
        INNER JOIN merchant m ON md.merchant_id = m.id
        WHERE m.name LIKE  '%' || :searchQuery || '%' OR md.details LIKE  '%' || :searchQuery || '%'
        ORDER BY id DESC
    """)
    fun searchMerchantDataWithMerchantNameList(searchQuery : String, timeZoneOffsetInMilli : Int): PagingSource<Int,MerchantDataWithName>

    @Query("""
        SELECT 
            strftime('%d-%m-%Y',  (date_milli + :timeZoneOffsetInMilli) / 1000, 'unixepoch') as day,
            SUM(CASE WHEN type >= 0 THEN amount ELSE 0 END) as totalIncome,
            SUM(CASE WHEN type < 0 THEN amount ELSE 0 END) as totalExpense
        FROM merchant_data
        GROUP BY day
        ORDER BY day DESC
    """)
    fun getMerchantDataDailyTotalList(timeZoneOffsetInMilli : Int): PagingSource<Int, MerchantDataDailyTotal>
}