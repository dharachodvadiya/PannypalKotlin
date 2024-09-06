package com.indie.apps.pennypal.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "user",
    foreignKeys = [
        ForeignKey(
            entity = Payment::class,
            parentColumns = ["id"],
            childColumns = ["payment_id"],
            onDelete = ForeignKey.NO_ACTION
        )
    ],
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

    val currency: String,

    @ColumnInfo(name = "payment_id")
    val paymentId: Long = 1L,
)
