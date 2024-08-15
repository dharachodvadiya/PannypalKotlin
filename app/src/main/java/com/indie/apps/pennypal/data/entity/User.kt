package com.indie.apps.pennypal.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "user"
)
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,

    val email: String? = null,

    @ColumnInfo(name = "last_sync_date_milli")
    val lastSyncDateInMilli: Long = 0,

    @ColumnInfo(name = "income_amt")
    val incomeAmount: Double = 0.0,

    @ColumnInfo(name = "expense_amt")
    val expenseAmount: Double = 0.0,

    val currency: String
)
