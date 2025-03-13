package com.indie.apps.pennypal.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.indie.apps.pennypal.data.database.entity.BaseCurrency

@Dao
interface BaseCurrencyDao : BaseDao<BaseCurrency> {

    @Query("INSERT OR IGNORE INTO base_currency (currency_country_code) VALUES (:countryCode)")
    suspend fun insertOrIgnoreQuery(countryCode: String): Long

    @Transaction
    @Query("SELECT * FROM base_currency where currency_country_code = :countryCode")
    suspend fun getBaseCurrencyFromCode(countryCode: String): BaseCurrency

    @Transaction
    @Query("SELECT * FROM base_currency where id = :id")
    suspend fun getBaseCurrencyFromCode(id: Long): BaseCurrency
}