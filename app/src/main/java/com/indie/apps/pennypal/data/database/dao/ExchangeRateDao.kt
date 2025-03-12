package com.indie.apps.pennypal.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.indie.apps.pennypal.data.database.entity.ExchangeRate

@Dao
interface ExchangeRateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(rate: ExchangeRate)

    @Query("SELECT * FROM exchange_rates WHERE from_currency = :from AND to_currency = :to LIMIT 1")
    suspend fun getRate(from: String, to: String): ExchangeRate?

    @Query("SELECT MAX(last_updated) FROM exchange_rates")
    suspend fun getLastUpdateTime(): Long?
}