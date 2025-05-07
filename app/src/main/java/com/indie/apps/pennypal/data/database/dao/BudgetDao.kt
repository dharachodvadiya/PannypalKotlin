package com.indie.apps.pennypal.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.indie.apps.pennypal.data.database.db_entity.Budget
import com.indie.apps.pennypal.data.module.budget.BudgetWithCategoryResult
import com.indie.apps.pennypal.data.module.budget.BudgetWithSpentAndCategoryIds
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao : BaseDao<Budget> {
    @Transaction
    @Query("delete from budget where id = :id")
    suspend fun deleteBudgetFromId(id: Long): Int

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
        oc.currency_symbol as originalAmountSymbol,
        b.original_currency_id as originalCurrencyId,
        GROUP_CONCAT(bc.category_id) AS categoryIds,
        0.0 As spentAmount
    FROM budget b
    LEFT JOIN budget_category bc ON b.id = bc.budget_id
    INNER JOIN base_currency oc  ON b.original_currency_id = oc.id
    WHERE 
        (
            b.period_type = 1
            AND strftime('%Y', (b.start_date + :timeZoneOffsetInMilli) / 1000, 'unixepoch') = :year
            AND strftime('%m', (b.start_date + :timeZoneOffsetInMilli) / 1000, 'unixepoch') = printf('%02d', :monthPlusOne)
        )
        OR
        (
            b.period_type = 2
            AND strftime('%Y', (b.start_date + :timeZoneOffsetInMilli) / 1000, 'unixepoch') = :year
        )
        OR
        (
            b.period_type = 3  -- ONE_TIME budget
            AND strftime('%m', (b.start_date + :timeZoneOffsetInMilli) / 1000, 'unixepoch') <= printf('%02d', :monthPlusOne)
            AND strftime('%Y', (b.start_date + :timeZoneOffsetInMilli) / 1000, 'unixepoch') <= :year
            AND (
                (strftime('%m', (b.end_date + :timeZoneOffsetInMilli) / 1000, 'unixepoch') >= printf('%02d', :monthPlusOne)
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
        oc.currency_symbol as originalAmountSymbol,
        b.original_currency_id as originalCurrencyId,
        GROUP_CONCAT(bc.category_id) AS categoryIds,
        0.0 As spentAmount
    FROM budget b
    LEFT JOIN budget_category bc ON b.id = bc.budget_id
    INNER JOIN base_currency oc  ON b.original_currency_id = oc.id
    WHERE 
        (
            b.period_type = 1
            AND strftime('%Y', (b.start_date + :timeZoneOffsetInMilli) / 1000, 'unixepoch') = :year
            AND strftime('%m', (b.start_date + :timeZoneOffsetInMilli) / 1000, 'unixepoch') = printf('%02d', :monthPlusOne)
        )
    GROUP BY b.id
    """
    )
    fun getMonthBudgetsWithCategoryIdListFromMonth(
        year: String,
        monthPlusOne: String,
        timeZoneOffsetInMilli: Int
    ): Flow<List<BudgetWithSpentAndCategoryIds>>

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
        oc.currency_symbol as originalAmountSymbol,
        b.original_currency_id as originalCurrencyId,
        GROUP_CONCAT(bc.category_id) AS categoryIds,
        0.0 As spentAmount
    FROM budget b
    LEFT JOIN budget_category bc ON b.id = bc.budget_id
    INNER JOIN base_currency oc  ON b.original_currency_id = oc.id
    WHERE
        (
            b.period_type = 2
            AND strftime('%Y', (b.start_date + :timeZoneOffsetInMilli) / 1000, 'unixepoch') = :year
        )
    GROUP BY b.id
    """
    )
    fun getYearBudgetsWithCategoryIdListFromYear(
        year: String,
        timeZoneOffsetInMilli: Int
    ): Flow<List<BudgetWithSpentAndCategoryIds>>

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
        oc.currency_symbol as originalAmountSymbol,
        b.original_currency_id as originalCurrencyId,
        GROUP_CONCAT(bc.category_id) AS categoryIds,
        0.0 As spentAmount
    FROM budget b
    LEFT JOIN budget_category bc ON b.id = bc.budget_id
    INNER JOIN base_currency oc  ON b.original_currency_id = oc.id
    WHERE
        (
            b.period_type = 3  -- ONE_TIME budget
            AND strftime('%m', (b.start_date + :timeZoneOffsetInMilli) / 1000, 'unixepoch') <= printf('%02d', :monthPlusOne)
            AND strftime('%Y', (b.start_date + :timeZoneOffsetInMilli) / 1000, 'unixepoch') <= :year
            AND (
                (strftime('%m', (b.end_date + :timeZoneOffsetInMilli) / 1000, 'unixepoch') >= printf('%02d', :monthPlusOne)
                    AND strftime('%Y', (b.end_date + :timeZoneOffsetInMilli) / 1000, 'unixepoch') >= :year) 
                OR 
                (strftime('%Y', (b.end_date + :timeZoneOffsetInMilli) / 1000, 'unixepoch') > :year)
            )
        )
    GROUP BY b.id
    """
    )
    fun getOneTimeBudgetsWithCategoryIdListFromMonth(
        year: String,
        monthPlusOne: String,
        timeZoneOffsetInMilli: Int
    ): Flow<List<BudgetWithSpentAndCategoryIds>>

    @Transaction
    @Query(
        """
        SELECT 
            b.id As budgetId,
            b.title ,
            b.amount As budgetAmount,
            b.period_type As periodType,
            b.start_date As startDate,
            b.end_date As endDate,
            b.created_date As createdDate,
            b.original_currency_id As originalCurrencyId,
            oc.currency_symbol as originalAmountSymbol,
            bc.category_id As categoryId,
            bc.amount As categoryAmount,
            c.name AS categoryName,
            c.type AS categoryType,
            c.icon_id As categoryIconId,
            c.icon_color_id As categoryIconColorId
        FROM budget b
        LEFT JOIN budget_category bc ON b.id = bc.budget_id
        LEFT JOIN category c ON c.id = bc.category_id
        INNER JOIN base_currency oc  ON b.original_currency_id = oc.id
        WHERE b.id = :budgetId
    """
    )
    fun getBudgetWithCategoryFromId(budgetId: Long): Flow<List<BudgetWithCategoryResult>>

    @Transaction
    @Query(
        """
    SELECT *
    FROM budget b
    WHERE 
        b.period_type = 1
            AND strftime('%Y', (b.start_date + :timeZoneOffsetInMilli) / 1000, 'unixepoch') = :year
            AND strftime('%m', (b.start_date + :timeZoneOffsetInMilli) / 1000, 'unixepoch') = printf('%02d', :monthPlusOne)
    """
    )
    fun getBudgetDataFromMonth(
        year: String,
        monthPlusOne: String,
        timeZoneOffsetInMilli: Int
    ): Flow<List<Budget>>

    @Transaction
    @Query(
        """
    SELECT *
    FROM budget b
    WHERE 
        b.period_type = 2
            AND strftime('%Y', (b.start_date + :timeZoneOffsetInMilli) / 1000, 'unixepoch') = :year
    """
    )
    fun getBudgetDataFromYear(
        year: String,
        timeZoneOffsetInMilli: Int
    ): Flow<List<Budget>>

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
        oc.currency_symbol as originalAmountSymbol,
        b.original_currency_id as originalCurrencyId,
        GROUP_CONCAT(bc.category_id) AS categoryIds,
        0.0 As spentAmount
    FROM budget b
    LEFT JOIN budget_category bc ON b.id = bc.budget_id
    INNER JOIN base_currency oc  ON b.original_currency_id = oc.id
    WHERE 
        (
            b.period_type = 1
            AND b.period_type = :periodType
            AND (
                strftime('%Y', (b.start_date + :timeZoneOffsetInMilli) / 1000, 'unixepoch') < :year
                OR (
                    strftime('%Y', (b.start_date + :timeZoneOffsetInMilli) / 1000, 'unixepoch') = :year
                    AND strftime('%m', (b.start_date + :timeZoneOffsetInMilli) / 1000, 'unixepoch') < printf('%02d', :monthPlusOne)
                )
            )
        )
        OR
        (
            b.period_type = 2
            AND b.period_type = :periodType
            AND strftime('%Y', (b.start_date + :timeZoneOffsetInMilli) / 1000, 'unixepoch') < :year
        )
        OR
        (
            b.period_type = 3
            AND b.period_type = :periodType
            AND (
                strftime('%Y', (b.end_date + :timeZoneOffsetInMilli) / 1000, 'unixepoch') < :year
                OR (
                    strftime('%Y', (b.end_date + :timeZoneOffsetInMilli) / 1000, 'unixepoch') = :year
                    AND strftime('%m', (b.end_date + :timeZoneOffsetInMilli) / 1000, 'unixepoch') < printf('%02d', :monthPlusOne)
                )
            )
        )
    GROUP BY b.id
    ORDER BY b.start_date DESC
    """
    )
    fun getPastBudgetsWithCategoryIdListFromPeriodType(
        year: String,
        monthPlusOne: String,
        periodType: Int,
        timeZoneOffsetInMilli: Int
    ): PagingSource<Int, BudgetWithSpentAndCategoryIds>

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
        oc.currency_symbol as originalAmountSymbol,
        b.original_currency_id as originalCurrencyId,
        GROUP_CONCAT(bc.category_id) AS categoryIds,
        0.0 As spentAmount
    FROM budget b
    LEFT JOIN budget_category bc ON b.id = bc.budget_id
    INNER JOIN base_currency oc  ON b.original_currency_id = oc.id
    WHERE 
        (
            b.period_type = 1
            AND b.period_type == :periodType
            AND (
                strftime('%Y', (b.start_date + :timeZoneOffsetInMilli) / 1000, 'unixepoch') > :year
                OR (
                    strftime('%Y', (b.start_date + :timeZoneOffsetInMilli) / 1000, 'unixepoch') = :year
                    AND strftime('%m', (b.start_date + :timeZoneOffsetInMilli) / 1000, 'unixepoch') > printf('%02d', :monthPlusOne)
                    )
                )
        )
        OR
        (
            b.period_type = 2
            AND b.period_type == :periodType
            AND strftime('%Y', (b.start_date + :timeZoneOffsetInMilli) / 1000, 'unixepoch') > :year
        )
        OR
        (
            b.period_type = 3
            AND b.period_type == :periodType
            AND (
                strftime('%Y', (b.start_date + :timeZoneOffsetInMilli) / 1000, 'unixepoch') > :year
                OR (
                    strftime('%Y', (b.start_date + :timeZoneOffsetInMilli) / 1000, 'unixepoch') = :year
                    AND strftime('%m', (b.start_date + :timeZoneOffsetInMilli) / 1000, 'unixepoch') > printf('%02d', :monthPlusOne)
                )
            )
        )
    GROUP BY b.id
    ORDER BY b.start_date DESC
    """
    )
    fun getUpComingBudgetsWithCategoryIdListFromPeriodType(
        year: String,
        monthPlusOne: String,
        periodType: Int,
        timeZoneOffsetInMilli: Int
    ): PagingSource<Int, BudgetWithSpentAndCategoryIds>
}