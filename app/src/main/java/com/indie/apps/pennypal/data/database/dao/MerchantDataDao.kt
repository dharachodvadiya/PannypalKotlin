package com.indie.apps.pennypal.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.indie.apps.pennypal.data.database.entity.MerchantData
import com.indie.apps.pennypal.data.module.IncomeAndExpense
import com.indie.apps.pennypal.data.module.merchant_data.MerchantDataWithAllData
import com.indie.apps.pennypal.data.module.merchant_data.MerchantDataWithName
import com.indie.apps.pennypal.data.module.merchant_data.MerchantDataWithNameWithDayTotal
import com.indie.apps.pennypal.data.module.merchant_data.MerchantDataWithPaymentName
import com.indie.apps.pennypal.data.module.balance.TotalAllTime
import com.indie.apps.pennypal.data.module.balance.TotalMonthly
import com.indie.apps.pennypal.data.module.balance.TotalYearly
import com.indie.apps.pennypal.data.module.category.CategoryAllTime
import com.indie.apps.pennypal.data.module.category.CategoryAmount
import com.indie.apps.pennypal.data.module.category.CategoryMonthly
import com.indie.apps.pennypal.data.module.category.CategoryYearly
import kotlinx.coroutines.flow.Flow

@Dao
interface MerchantDataDao : BaseDao<MerchantData> {

    //insert data -> update merchant income-expense
    //update data -> update merchant income-expense
    //delete data -> update merchant income-expense
    //get data

    @Transaction
    @Query("UPDATE merchant_data SET payment_id = :newPaymentId WHERE payment_id = :oldPaymentId")
    suspend fun updateMerchantDataPaymentId(oldPaymentId: Long, newPaymentId: Long): Int

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
    @Query("SELECT * FROM merchant_data ORDER BY date_milli DESC")
    fun getMerchantDataList(): PagingSource<Int, MerchantData>

    @Transaction
    @Query("SELECT * FROM merchant_data where merchant_id = :merchantId ORDER BY date_milli DESC")
    fun getMerchantDataListFromMerchantId(merchantId: Long): PagingSource<Int, MerchantData>

    @Transaction
    @Query(
        """
        SELECT md.id as id, 
                md.merchant_id as merchantId, 
                m.name as merchantName, 
                md.category_id as categoryId, 
                c.name as categoryName,
                c.icon_id as categoryIconId,
                c.icon_color_id as categoryIconColorId,
                md.payment_id as paymentId, 
                p.name as paymentName, 
                md.date_milli as dateInMilli,
                md.details, 
                md.amount, 
                md.type
        FROM merchant_data md
        LEFT JOIN merchant m ON md.merchant_id = m.id
        INNER JOIN category c ON md.category_id = c.id
        INNER JOIN payment_type p ON md.payment_id = p.id
        WHERE m.name LIKE  '%' || :searchQuery || '%' OR 
                md.details LIKE  '%' || :searchQuery || '%' OR
                c.name LIKE  '%' || :searchQuery || '%' OR
                p.name LIKE  '%' || :searchQuery || '%'
        ORDER BY md.date_milli DESC
    """
    )

    fun searchMerchantsDataWithAllDataList(searchQuery: String): PagingSource<Int, MerchantDataWithAllData>

    @Transaction
    @Query(
        """
        SELECT md.id as id, 
                md.merchant_id as merchantId, 
                m.name as merchantName, 
                md.category_id as categoryId, 
                c.name as categoryName,
                c.icon_id as categoryIconId,
                c.icon_color_id as categoryIconColorId,
                md.payment_id as paymentId, 
                p.name as paymentName, 
                md.date_milli as dateInMilli,
                md.details, 
                md.amount, 
                md.type
        FROM merchant_data md
        LEFT JOIN merchant m ON md.merchant_id = m.id
        INNER JOIN category c ON md.category_id = c.id
        INNER JOIN payment_type p ON md.payment_id = p.id
        ORDER BY md.date_milli DESC LIMIT 3
    """
    )

    fun getRecentMerchantsDataWithAllDataList(): Flow<List<MerchantDataWithAllData>>

    @Transaction
    @Query(
        """
        SELECT md.id as id, 
                md.merchant_id as merchantId, 
                m.name as merchantName, 
                md.category_id as categoryId, 
                c.name as categoryName,
                c.icon_id as categoryIconId,
                c.icon_color_id as categoryIconColorId,
                md.payment_id as paymentId, 
                p.name as paymentName, 
                md.date_milli as dateInMilli,
                md.details, 
                md.amount, 
                md.type
        FROM merchant_data md
        LEFT JOIN merchant m ON md.merchant_id = m.id
        INNER JOIN category c ON md.category_id = c.id
        INNER JOIN payment_type p ON md.payment_id = p.id
        WHERE strftime('%m', (md.date_milli + :timeZoneOffsetInMilli) / 1000, 'unixepoch') = printf('%02d', :monthPlusOne)
        ORDER BY md.date_milli DESC LIMIT 3
    """
    )

    fun getRecentMerchantsDataWithAllDataListFromMonth(
        timeZoneOffsetInMilli: Int,
        monthPlusOne: String
    ): Flow<List<MerchantDataWithAllData>>

    @Transaction
    @Query(
        """
        SELECT md.id as id, 
                md.merchant_id as merchantId, 
                m.name as merchantName, 
                md.category_id as categoryId, 
                c.name as categoryName,
                c.icon_id as categoryIconId,
                c.icon_color_id as categoryIconColorId,
                md.payment_id as paymentId, 
                p.name as paymentName, 
                md.date_milli as dateInMilli,
                md.details, 
                md.amount, 
                md.type
        FROM merchant_data md
        LEFT JOIN merchant m ON md.merchant_id = m.id
        INNER JOIN category c ON md.category_id = c.id
        INNER JOIN payment_type p ON md.payment_id = p.id
        WHERE  strftime('%Y', (md.date_milli + :timeZoneOffsetInMilli) / 1000, 'unixepoch') = :year
        ORDER BY md.date_milli DESC LIMIT 3
    """
    )

    fun getRecentMerchantsDataWithAllDataListFromYear(
        timeZoneOffsetInMilli: Int,
        year: String
    ): Flow<List<MerchantDataWithAllData>>

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
        ORDER BY md.date_milli DESC
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
        LEFT JOIN merchant m ON md.merchant_id = m.id
        WHERE m.name LIKE  '%' || :searchQuery || '%' OR md.details LIKE  '%' || :searchQuery || '%'
        ORDER BY md.date_milli DESC
    """
    )
    fun searchMerchantDataWithMerchantNameList(
        searchQuery: String,
        timeZoneOffsetInMilli: Int
    ): PagingSource<Int, MerchantDataWithName>

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
            LEFT JOIN 
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


    @Query(
        """
    SELECT 
        strftime('%Y-%m', (m.date_milli + :timeZoneOffsetInMilli) / 1000, 'unixepoch') AS month,
        IFNULL(SUM(CASE WHEN m.type >= 0 THEN m.amount ELSE 0 END), 0) AS totalIncome,
        IFNULL(SUM(CASE WHEN m.type < 0 THEN m.amount ELSE 0 END), 0) AS totalExpense
    FROM 
        merchant_data m
    JOIN 
        user u ON u.id = 1
    WHERE 
        strftime('%Y', (m.date_milli + :timeZoneOffsetInMilli) / 1000, 'unixepoch') = :year
        AND strftime('%m', (m.date_milli + :timeZoneOffsetInMilli) / 1000, 'unixepoch') = printf('%02d', :monthPlusOne)
    GROUP BY 
        month
    ORDER BY 
        month DESC
    """
    )
    fun getTotalFromMonthAsFlow(
        timeZoneOffsetInMilli: Int,
        year: String,
        monthPlusOne: String
    ): Flow<TotalMonthly>

    @Transaction
    @Query(
        """
    SELECT 
        strftime('%Y-%m', (m.date_milli + :timeZoneOffsetInMilli) / 1000, 'unixepoch') AS month,
        IFNULL(SUM(CASE WHEN m.type >= 0 THEN m.amount ELSE 0 END), 0) AS totalIncome,
        IFNULL(SUM(CASE WHEN m.type < 0 THEN m.amount ELSE 0 END), 0) AS totalExpense
    FROM 
        merchant_data m
    JOIN 
        user u ON u.id = 1
    WHERE 
        strftime('%Y', (m.date_milli + :timeZoneOffsetInMilli) / 1000, 'unixepoch') = :year
        AND strftime('%m', (m.date_milli + :timeZoneOffsetInMilli) / 1000, 'unixepoch') = printf('%02d', :monthPlusOne)
    GROUP BY 
        month
    ORDER BY 
        month DESC
    """
    )
    suspend fun getTotalFromMonth(
        timeZoneOffsetInMilli: Int,
        year: String,
        monthPlusOne: String
    ): TotalMonthly

    @Query(
        """
    SELECT 
        strftime('%Y', (m.date_milli + :timeZoneOffsetInMilli) / 1000, 'unixepoch') AS year,
        IFNULL(SUM(CASE WHEN m.type >= 0 THEN m.amount ELSE 0 END), 0) AS totalIncome,
        IFNULL(SUM(CASE WHEN m.type < 0 THEN m.amount ELSE 0 END), 0) AS totalExpense
    FROM 
        merchant_data m
    JOIN 
        user u ON u.id = 1
    WHERE 
        strftime('%Y', (m.date_milli + :timeZoneOffsetInMilli) / 1000, 'unixepoch') = :year
    GROUP BY 
        year
    ORDER BY 
        year DESC
    """
    )
    fun getTotalFromYearAsFlow(
        timeZoneOffsetInMilli: Int,
        year: String
    ): Flow<TotalYearly>

    @Transaction
    @Query(
        """
    SELECT 
        strftime('%Y', (m.date_milli + :timeZoneOffsetInMilli) / 1000, 'unixepoch') AS year,
        IFNULL(SUM(CASE WHEN m.type >= 0 THEN m.amount ELSE 0 END), 0) AS totalIncome,
        IFNULL(SUM(CASE WHEN m.type < 0 THEN m.amount ELSE 0 END), 0) AS totalExpense
    FROM 
        merchant_data m
    JOIN 
        user u ON u.id = 1
    WHERE 
        strftime('%Y', (m.date_milli + :timeZoneOffsetInMilli) / 1000, 'unixepoch') = :year
    GROUP BY 
        year
    ORDER BY 
        year DESC
    """
    )
    suspend fun getTotalFromYear(
        timeZoneOffsetInMilli: Int,
        year: String
    ): TotalYearly


    @Query(
        """
        SELECT
            IFNULL(SUM(CASE WHEN m.type >= 0 THEN m.amount ELSE 0 END), 0) AS totalIncome,
            IFNULL(SUM(CASE WHEN m.type < 0 THEN m.amount ELSE 0 END), 0) AS totalExpense
        FROM 
            merchant_data m
        JOIN 
            user u ON u.id = 1
    """
    )
    fun getTotalAsFlow(): Flow<TotalAllTime>

    @Transaction
    @Query(
        """
        SELECT
            IFNULL(SUM(CASE WHEN m.type >= 0 THEN m.amount ELSE 0 END), 0) AS totalIncome,
            IFNULL(SUM(CASE WHEN m.type < 0 THEN m.amount ELSE 0 END), 0) AS totalExpense
        FROM 
            merchant_data m
        JOIN 
            user u ON u.id = 1
    """
    )
    suspend fun getTotal(): TotalAllTime

    @Query(
        """
        SELECT 
            c.id AS id,
            strftime('%Y-%m', (md.date_milli + :timeZoneOffsetInMilli) / 1000, 'unixepoch') as month,
            c.name AS name,
            c.type As type,
            c.icon_id As iconId,
            c.icon_color_id As iconColorId,
            SUM(CASE WHEN md.type = -1 THEN md.amount ELSE 0 END) AS amount
        FROM 
            merchant_data md
        JOIN 
            category c ON md.category_id = c.id
        WHERE 
             strftime('%Y', (md.date_milli + :timeZoneOffsetInMilli) / 1000, 'unixepoch') = :year
            AND strftime('%m', (md.date_milli + :timeZoneOffsetInMilli) / 1000, 'unixepoch') = printf('%02d', :monthPlusOne)
            AND c.type != 1
        GROUP BY 
            month, c.name
        ORDER BY 
            amount DESC
    """
    )

    fun getCategoryWiseExpenseFromMonthAsFlow(
        timeZoneOffsetInMilli: Int,
        year: String,
        monthPlusOne: String
    ): Flow<List<CategoryMonthly>>

    @Transaction
    @Query(
        """
        SELECT 
            c.id AS id,
            strftime('%Y-%m', (md.date_milli + :timeZoneOffsetInMilli) / 1000, 'unixepoch') as month,
            c.name AS name,
            c.type As type,
            c.icon_id As iconId,
            c.icon_color_id As iconColorId,
            SUM(CASE WHEN md.type = -1 THEN md.amount ELSE 0 END) AS amount
        FROM 
            merchant_data md
        JOIN 
            category c ON md.category_id = c.id
        WHERE 
             strftime('%Y', (md.date_milli + :timeZoneOffsetInMilli) / 1000, 'unixepoch') = :year
            AND strftime('%m', (md.date_milli + :timeZoneOffsetInMilli) / 1000, 'unixepoch') = printf('%02d', :monthPlusOne)
            AND c.type != 1
        GROUP BY 
            month, c.name
        ORDER BY 
            amount DESC
    """
    )

    suspend fun getCategoryWiseExpenseFromMonth(
        timeZoneOffsetInMilli: Int,
        year: String,
        monthPlusOne: String
    ): List<CategoryMonthly>

    @Query(
        """
        SELECT 
            c.id AS id,
            strftime('%Y', (md.date_milli + :timeZoneOffsetInMilli) / 1000, 'unixepoch') AS year,
            c.name AS name,
            c.type As type,
            c.icon_id As iconId,
            c.icon_color_id As iconColorId,
            SUM(CASE WHEN md.type = -1 THEN md.amount ELSE 0 END) AS amount
        FROM 
            merchant_data md
        JOIN 
            category c ON md.category_id = c.id
        WHERE 
            strftime('%Y', (md.date_milli + :timeZoneOffsetInMilli) / 1000, 'unixepoch') = :year
            AND c.type != 1
        GROUP BY 
            year, c.name
        ORDER BY 
            amount DESC
    """
    )

    fun getCategoryWiseExpenseFromYearAsFlow(
        timeZoneOffsetInMilli: Int,
        year: String
    ): Flow<List<CategoryYearly>>

    @Transaction
    @Query(
        """
        SELECT 
            c.id AS id,
            strftime('%Y', (md.date_milli + :timeZoneOffsetInMilli) / 1000, 'unixepoch') AS year,
            c.name AS name,
            c.type As type,
            c.icon_id As iconId,
            c.icon_color_id As iconColorId,
            SUM(CASE WHEN md.type = -1 THEN md.amount ELSE 0 END) AS amount
        FROM 
            merchant_data md
        JOIN 
            category c ON md.category_id = c.id
        WHERE 
            strftime('%Y', (md.date_milli + :timeZoneOffsetInMilli) / 1000, 'unixepoch') = :year
            AND c.type != 1
        GROUP BY 
            year, c.name
        ORDER BY 
            amount DESC
    """
    )

    suspend fun getCategoryWiseExpenseFromYear(
        timeZoneOffsetInMilli: Int,
        year: String
    ): List<CategoryYearly>

    @Query(
        """
        SELECT 
            c.id AS id,
            c.name AS name,
            c.type As type,
            c.icon_id As iconId,
            c.icon_color_id As iconColorId,
            SUM(CASE WHEN md.type = -1 THEN md.amount ELSE 0 END) AS amount
        FROM 
            merchant_data md
        JOIN 
            category c ON md.category_id = c.id
        WHERE 
            c.type != 1
        GROUP BY 
            c.name
        ORDER BY 
            amount DESC
    """
    )

    fun getCategoryWiseExpenseAsFlow(): Flow<List<CategoryAllTime>>

    @Transaction
    @Query(
        """
        SELECT 
            c.id AS id,
            c.name AS name,
            c.type As type,
            c.icon_id As iconId,
            c.icon_color_id As iconColorId,
            SUM(CASE WHEN md.type = -1 THEN md.amount ELSE 0 END) AS amount
        FROM 
            merchant_data md
        JOIN 
            category c ON md.category_id = c.id
        WHERE 
            c.type != 1
        GROUP BY 
            c.name
        ORDER BY 
            amount DESC
    """
    )

    suspend fun getCategoryWiseExpense(): List<CategoryAllTime>

    @Query(
        """
        SELECT IFNULL(SUM(amount), 0.0) 
        FROM merchant_data
        WHERE
            strftime('%Y', ((date_milli + :timeZoneOffsetInMilli) / 1000), 'unixepoch') = :year
            AND strftime('%m', ((date_milli + :timeZoneOffsetInMilli) / 1000), 'unixepoch') = printf('%02d', :monthPlusOne)
            AND category_id IN (:categoryIds)
            AND type = -1
    """
    )
    suspend fun getTotalAmountForMonthAndCategory(
        timeZoneOffsetInMilli: Int,
        year: String,
        monthPlusOne: String,
        categoryIds: List<Long>
    ): Double

    @Query(
        """
        SELECT IFNULL(SUM(amount), 0.0) 
        FROM merchant_data
        WHERE
            strftime('%Y', ((date_milli + :timeZoneOffsetInMilli) / 1000), 'unixepoch') = :year
            AND category_id IN (:categoryIds)
            AND type = -1
    """
    )
    suspend fun getTotalAmountForYearAndCategory(
        timeZoneOffsetInMilli: Int,
        year: String,
        categoryIds: List<Long>
    ): Double

    @Query(
        """
        SELECT IFNULL(SUM(amount), 0.0) 
        FROM merchant_data
        WHERE
            date_milli BETWEEN :startTime AND :endTime
            AND category_id IN (:categoryIds)
            AND type = -1
    """
    )
    suspend fun getTotalAmountForBetweenDatesAndCategory(
        startTime: Long,
        endTime: Long,
        categoryIds: List<Long>
    ): Double

    @Query(
        """
        SELECT 
            c.id AS id,
            c.name,
            c.icon_id As iconId,
            c.icon_color_id As iconColorId,
            c.type,
            SUM(md.amount) AS amount
        FROM 
            merchant_data md
        JOIN 
            category c ON md.category_id = c.id
        WHERE
            strftime('%Y', ((md.date_milli + :timeZoneOffsetInMilli) / 1000), 'unixepoch') = :year
            AND strftime('%m', ((md.date_milli + :timeZoneOffsetInMilli) / 1000), 'unixepoch') = printf('%02d', :monthPlusOne)
            AND md.category_id IN (:categoryIds)
            AND md.type = -1
        GROUP BY 
            c.id, c.name, c.type
    """
    )
    fun getCategoryWiseTotalAmountForMonth(
        timeZoneOffsetInMilli: Int,
        year: String,
        monthPlusOne: String,
        categoryIds: List<Long>
    ): Flow<List<CategoryAmount>>

    @Query(
        """
        SELECT 
            c.id AS id,
            c.name,
            c.type,
            c.icon_id As iconId,
            c.icon_color_id As iconColorId,
            SUM(md.amount) AS amount
        FROM 
            merchant_data md
        JOIN 
            category c ON md.category_id = c.id
        WHERE
            strftime('%Y', ((md.date_milli + :timeZoneOffsetInMilli) / 1000), 'unixepoch') = :year
            AND md.category_id IN (:categoryIds)
            AND md.type = -1
        GROUP BY 
            c.id, c.name, c.type
    """
    )
    fun getCategoryWiseTotalAmountForYear(
        timeZoneOffsetInMilli: Int,
        year: String,
        categoryIds: List<Long>
    ): Flow<List<CategoryAmount>>

    @Query(
        """
        SELECT 
            c.id AS id,
            c.name,
            c.type,
            c.icon_id As iconId,
            c.icon_color_id As iconColorId,
            SUM(md.amount) AS amount
        FROM 
            merchant_data md
        JOIN 
            category c ON md.category_id = c.id
        WHERE
            md.date_milli BETWEEN :startTime AND :endTime
            AND md.category_id IN (:categoryIds)
            AND md.type = -1
        GROUP BY 
            c.id, c.name, c.type
    """
    )
    fun getCategoryWiseTotalAmountForBetweenDates(
        startTime: Long,
        endTime: Long,
        categoryIds: List<Long>
    ): Flow<List<CategoryAmount>>
}