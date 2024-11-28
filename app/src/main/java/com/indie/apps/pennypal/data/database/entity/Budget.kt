package com.indie.apps.pennypal.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.indie.apps.pennypal.data.module.budget.BudgetWithSpentAndCategoryIds

@Entity(
    tableName = "budget",
)
data class Budget(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val title: String,

    val amount: Double,

    @ColumnInfo(name = "period_type")
    val periodType: Int, // Enum: MONTH, YEAR, ONE_TIME

    @ColumnInfo(name = "start_date")
    val startDate: Long,

    @ColumnInfo(name = "end_date")
    val endDate: Long? = null, // Only for one-time budgets

    @ColumnInfo(name = "created_date")
    val createdDate: Long,

    )

