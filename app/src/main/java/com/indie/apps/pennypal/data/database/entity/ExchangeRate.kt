package com.indie.apps.pennypal.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "exchange_rates"
)
data class ExchangeRate(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "from_currency")
    val fromCurrency: String,  // e.g., "INR"

    @ColumnInfo(name = "to_currency")
    val toCurrency: String,    // e.g., "USD"

    val rate: Double,          // e.g., 83.25

    @ColumnInfo(name = "last_updated")
    val lastUpdated: Long      // Timestamp in milliseconds

)