package com.indie.apps.pennypal.data.database.db_entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "base_currency",
    indices = [Index(value = ["currency_country_code"], unique = true)]
)
data class BaseCurrency(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "currency_country_code", collate = ColumnInfo.NOCASE)
    val currencyCountryCode: String,

    @ColumnInfo(name = "currency_symbol")
    val currencySymbol: String,

    )