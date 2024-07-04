package com.indie.apps.pannypal.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "merchant",
    indices = [Index(value = ["name"], unique = true)]
)
data class Merchant(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,

    @ColumnInfo(name = "phone_num")
    val phoneNumber: Int?,

    @ColumnInfo(name = "date_milli")
    val dateInMilli: Long,

    val details: String?,

    @ColumnInfo(name = "income_amt")
    val incomeAmount: Long,

    @ColumnInfo(name = "expense_amt")
    val expenseAmount: Long,
)
