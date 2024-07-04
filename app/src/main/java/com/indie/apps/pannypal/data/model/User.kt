package com.indie.apps.pannypal.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "user"
)
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,

    val email: String?,

    @ColumnInfo(name = "last_sync_date_milli")
    val lastSyncDateInMilli: Long =0,

    @ColumnInfo(name = "income_amt")
    val incomeAmount: Long,

    @ColumnInfo(name = "expense_amt")
    val expenseAmount: Long,

    val currency: String
)
