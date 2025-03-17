package com.indie.apps.pennypal.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.indie.apps.pennypal.data.database.entity.BaseCurrency

@Dao
interface BaseCurrencyDao{

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreQuery(data: BaseCurrency): Long

    @Transaction
    @Query("SELECT * FROM base_currency where currency_country_code = :countryCode")
    suspend fun getBaseCurrencyFromCode(countryCode: String): BaseCurrency?

    @Transaction
    @Query("SELECT * FROM base_currency where id = :id")
    suspend fun getBaseCurrencyFromCode(id: Long): BaseCurrency?
}