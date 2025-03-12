package com.indie.apps.pennypal.repository

import com.indie.apps.pennypal.data.database.entity.BaseCurrency

interface BaseCurrencyRepository : BaseRepository<BaseCurrency> {
    fun getBaseCurrencyFromCode(countryCode: String): BaseCurrency

}