package com.indie.apps.pennypal.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.indie.apps.pennypal.data.entity.MerchantData
import com.indie.apps.pennypal.data.module.IncomeAndExpense
import com.indie.apps.pennypal.data.module.MerchantDataDailyTotal
import com.indie.apps.pennypal.data.module.MerchantDataWithName
import com.indie.apps.pennypal.data.module.MerchantDataWithNameWithDayTotal
import com.indie.apps.pennypal.data.module.MerchantDataWithPaymentName

@Dao
interface MerchantDataDao : BaseDao<MerchantData> {

    //insert data -> update merchant income-expense
    //update data -> update merchant income-expense
    //delete data -> update merchant income-expense
    //get data

    @Transaction
    @Query("delete from merchant_data where id = :id")
    suspend fun deleteMerchantDataWithId(id: Long): Int

    @Transaction
    @Query("delete from merchant_data where id In (:idList)")
    suspend fun deleteMerchantDataWithIdList(idList: List<Long>): Int

    @Transaction
    @Query("delete from merchant_data where merchant_id In (:idList)")
    suspend fun deleteMerchantDataWithMerchantIdList(idList: List<Long>): Int

    @Transaction
    @Query("delete from merchant_data where merchant_id = :id")
    suspend fun deleteMerchantDataWithMerchantId(id: Long): Int

    @Transaction
    @Query("SELECT * FROM merchant_data where id = :id")
    suspend fun getMerchantDataFromId(id: Long): MerchantData

    @Transaction
    @Query("SELECT * FROM merchant_data ORDER BY id DESC")
    fun getMerchantDataList(): PagingSource<Int, MerchantData>

    @Transaction
    @Query("SELECT * FROM merchant_data where merchant_id = :merchantId ORDER BY id DESC")
    fun getMerchantDataListFromMerchantId(merchantId: Long): PagingSource<Int, MerchantData>

    @Transaction
    @Query(
        """
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
    """
    )

    fun getMerchantsDataWithMerchantNameList(timeZoneOffsetInMilli: Int): PagingSource<Int, MerchantDataWithName>

    @Transaction
    @Query(
        """
        SELECT md.id as id, 
                md.payment_id as paymentId, 
                md.date_milli as dateInMilli,
                md.details, 
                md.amount, 
                md.type,
                p.name as paymentName
        FROM merchant_data md
        INNER JOIN payment_type p ON md.payment_id = p.id
        where md.merchant_id = :merchantId
        ORDER BY id DESC
    """
    )

    fun getMerchantsDataWithPaymentNameListFromMerchantId(merchantId: Long): PagingSource<Int, MerchantDataWithPaymentName>

    @Transaction
    @Query(
        """
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
    """
    )
    fun searchMerchantDataWithMerchantNameList(
        searchQuery: String,
        timeZoneOffsetInMilli: Int
    ): PagingSource<Int, MerchantDataWithName>

    @Query(
        """
        SELECT 
            strftime('%d-%m-%Y',  (date_milli + :timeZoneOffsetInMilli) / 1000, 'unixepoch') as day,
            SUM(CASE WHEN type >= 0 THEN amount ELSE 0 END) as totalIncome,
            SUM(CASE WHEN type < 0 THEN amount ELSE 0 END) as totalExpense,
            MAX(id) as lastId
        FROM merchant_data
        GROUP BY day
        ORDER BY day DESC
    """
    )
    fun getMerchantDataDailyTotalList(timeZoneOffsetInMilli: Int): PagingSource<Int, MerchantDataDailyTotal>

    @Transaction
    @Query(
        """
        SELECT
            SUM(CASE WHEN type >= 0 THEN amount ELSE 0 END) as totalIncome,
            SUM(CASE WHEN type < 0 THEN amount ELSE 0 END) as totalExpense
        FROM merchant_data
        WHERE ID IN (:ids)
    """
    )
    suspend fun getTotalIncomeAndeExpenseFromIds(ids: List<Long>): IncomeAndExpense

    @Query(
        """
        WITH DailyTotals AS (
            SELECT 
                strftime('%d-%m-%Y', (date_milli + :timeZoneOffsetInMilli) / 1000, 'unixepoch') as day,
                SUM(CASE WHEN type >= 0 THEN amount ELSE 0 END) as totalIncome,
                SUM(CASE WHEN type < 0 THEN amount ELSE 0 END) as totalExpense,
                NULL as id,          -- Placeholder for DailyTotals
                NULL as merchantId,          -- Placeholder for DailyTotals
                MAX(date_milli) as dateInMilli,          -- Placeholder for DailyTotals
                NULL as amount,      -- Placeholder for DailyTotals
                NULL as details,     -- Placeholder for DailyTotals
                NULL as type,     -- Placeholder for DailyTotals
                NULL as merchantName -- Placeholder for DailyTotals
            FROM 
                merchant_data
            GROUP BY day
        ),
        Records AS (
            SELECT 
                md.id as id, 
                md.merchant_id as merchantId, 
                md.date_milli as dateInMilli, 
                strftime('%d-%m-%Y', (md.date_milli + :timeZoneOffsetInMilli) / 1000, 'unixepoch') as day,
                md.details, 
                md.amount, 
                md.type,
                m.name as merchantName,
                NULL as totalIncome,  -- Placeholder for Records
                NULL as totalExpense  -- Placeholder for Records
            FROM 
                merchant_data md
            INNER JOIN 
                merchant m ON md.merchant_id = m.id
        )
        SELECT 
            id,
            merchantId,
            dateInMilli,
            day,
            details,
            amount,
            type,
            merchantName,
            totalIncome,
            totalExpense
        FROM DailyTotals
        UNION ALL
        SELECT 
            id,
            merchantId,
            dateInMilli,
            day,
            details,
            amount,
            type,
            merchantName,
            totalIncome,
            totalExpense
        FROM Records
        ORDER BY dateInMilli DESC
        """
    )
    fun getCombinedDataWithLimit(
        timeZoneOffsetInMilli: Int
    ): PagingSource<Int, MerchantDataWithNameWithDayTotal>
}