package com.indie.apps.pennypal.repository

import com.indie.apps.pennypal.data.database.db_entity.BaseCurrency

interface BaseCurrencyRepository {
    suspend fun getBaseCurrencyFromCode(countryCode: String): BaseCurrency?
    suspend fun getBaseCurrencyFromId(id: Long): BaseCurrency?
    suspend fun insert(data: BaseCurrency): Long

}