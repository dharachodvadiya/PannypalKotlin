package com.indie.apps.pennypal.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.indie.apps.pennypal.data.database.entity.Budget
import com.indie.apps.pennypal.data.module.budget.BudgetWithSpentAndCategoryIds
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao : BaseDao<Budget> {
    @Transaction
    @Query("delete from budget where id = :id")
    suspend fun deleteBudgetFromId(id: Long): Int

    /*@Transaction
    @Query(
        """
    SELECT * FROM budget
    WHERE 
        (
            period_type = 1
            AND strftime('%Y', (start_date + :timeZoneOffsetInMilli) / 1000, 'unixepoch') = :year
            AND strftime('%m', (start_date + :timeZoneOffsetInMilli) / 1000, 'unixepoch')  = :monthPlusOne
        )
        OR
        (
            period_type = 2
            AND strftime('%Y', (start_date + :timeZoneOffsetInMilli) / 1000, 'unixepoch') = :year
        )
        OR
        (
            period_type = 3  -- ONE_TIME budget
            AND strftime('%m', (start_date + :timeZoneOffsetInMilli) / 1000, 'unixepoch') <= :monthPlusOne
            AND strftime('%Y', (start_date + :timeZoneOffsetInMilli) / 1000, 'unixepoch') <= :year
            AND (
                (strftime('%m', (end_date + :timeZoneOffsetInMilli) / 1000, 'unixepoch') >= :monthPlusOne
                    AND strftime('%Y', (end_date + :timeZoneOffsetInMilli) / 1000, 'unixepoch') >= :year) 
                OR 
                (strftime('%Y', (end_date + :timeZoneOffsetInMilli) / 1000, 'unixepoch') > :year))
        )
        
    """
    )
    fun getBudgetListFromMonth(
        timeZoneOffsetInMilli: Int,
        year: String,
        monthPlusOne: String
    ): Flow<List<Budget>>*/

    @Transaction
    @Query(
        """
    SELECT 
        b.id, 
        b.title, 
        b.amount AS budgetAmount, 
        b.start_date As startDate,
        b.end_date As endDate,
        b.period_type As periodType, 
        GROUP_CONCAT(bc.category_id) AS categoryIds,
        0.0 As spentAmount
    FROM budget b
    LEFT JOIN budget_category bc ON b.id = bc.budget_id
    WHERE 
        (
            b.period_type = 1
            AND strftime('%Y', (b.start_date + :timeZoneOffsetInMilli) / 1000, 'unixepoch') = :year
            AND strftime('%m', (b.start_date + :timeZoneOffsetInMilli) / 1000, 'unixepoch') = :monthPlusOne
        )
        OR
        (
            b.period_type = 2
            AND strftime('%Y', (b.start_date + :timeZoneOffsetInMilli) / 1000, 'unixepoch') = :year
        )
        OR
        (
            b.period_type = 3  -- ONE_TIME budget
            AND strftime('%m', (b.start_date + :timeZoneOffsetInMilli) / 1000, 'unixepoch') <= :monthPlusOne
            AND strftime('%Y', (b.start_date + :timeZoneOffsetInMilli) / 1000, 'unixepoch') <= :year
            AND (
                (strftime('%m', (b.end_date + :timeZoneOffsetInMilli) / 1000, 'unixepoch') >= :monthPlusOne
                    AND strftime('%Y', (b.end_date + :timeZoneOffsetInMilli) / 1000, 'unixepoch') >= :year) 
                OR 
                (strftime('%Y', (b.end_date + :timeZoneOffsetInMilli) / 1000, 'unixepoch') > :year)
            )
        )
    GROUP BY b.id
    """
    )
    fun getBudgetsWithCategoryIdListFromMonth(
        year: String,
        monthPlusOne: String,
        timeZoneOffsetInMilli: Int
    ): Flow<List<BudgetWithSpentAndCategoryIds>>
}