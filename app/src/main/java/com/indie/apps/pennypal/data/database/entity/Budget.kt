package com.indie.apps.pennypal.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "budget",
    foreignKeys = [
        ForeignKey(
            entity = BaseCurrency::class,
            parentColumns = ["id"],
            childColumns = ["original_currency_id"],
            onDelete = ForeignKey.NO_ACTION
        ),
    ],
    indices = [
        Index(value = ["original_currency_id"])
    ]
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

    @ColumnInfo(name = "original_currency_id")
    val originalCurrencyId: Long,

    )

