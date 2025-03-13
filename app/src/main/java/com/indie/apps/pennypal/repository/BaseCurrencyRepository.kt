package com.indie.apps.pennypal.repository

import com.indie.apps.pennypal.data.database.entity.BaseCurrency

interface BaseCurrencyRepository : BaseRepository<BaseCurrency> {
    suspend fun getBaseCurrencyFromCode(countryCode: String): BaseCurrency
    suspend fun getBaseCurrencyFromId(id: Long): BaseCurrency

}