package com.indie.apps.pennypal.repository

import com.indie.apps.pennypal.data.database.dao.MerchantDataDao
import com.indie.apps.pennypal.data.database.entity.BaseCurrency
import com.indie.apps.pennypal.data.database.entity.MerchantData
import com.indie.apps.pennypal.data.module.Amount
import com.indie.apps.pennypal.data.module._interface.ConvertibleAmount
import com.indie.apps.pennypal.data.module.balance.Total
import com.indie.apps.pennypal.data.module.balance.mergeByAmount
import com.indie.apps.pennypal.data.module.category.CategoryAmount
import com.indie.apps.pennypal.data.module.category.mergeAndSortByAmount
import com.indie.apps.pennypal.data.module.mergeByAmount
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MerchantDataRepositoryImpl @Inject constructor(
    private val merchantDataDao: MerchantDataDao,
    private val exchangeRateRepository: ExchangeRateRepository,
    private val userRepository: UserRepository,
    private val baseCurrencyRepository: BaseCurrencyRepository,
    private val dispatcher: CoroutineDispatcher
) :
    MerchantDataRepository {
    override suspend fun updateMerchantDataPaymentId(oldPaymentId: Long, newPaymentId: Long) =
        withContext(dispatcher) {
            merchantDataDao.updateMerchantDataPaymentId(oldPaymentId, newPaymentId)
        }

    override suspend fun deleteMerchantDataWithId(id: Long) = withContext(dispatcher) {
        merchantDataDao.deleteMerchantDataWithId(id)
    }

    override suspend fun deleteMerchantDataWithIdList(idList: List<Long>) =
        withContext(dispatcher) {
            merchantDataDao.deleteMerchantDataWithIdList(idList)
        }

    override suspend fun deleteMerchantDataWithMerchantIdList(idList: List<Long>) =
        withContext(dispatcher) {
            merchantDataDao.deleteMerchantDataWithMerchantIdList(idList)
        }

    override suspend fun deleteMerchantDataWithMerchantId(id: Long) = withContext(dispatcher) {
        merchantDataDao.deleteMerchantDataWithMerchantId(id)
    }

    /* override suspend fun getTotalIncomeAndeExpenseFromIds(ids: List<Long>) =
         withContext(dispatcher) {
             merchantDataDao.getTotalIncomeAndeExpenseFromIds(ids)
         }*/

    override suspend fun getMerchantDataFromId(id: Long) = withContext(dispatcher) {
        merchantDataDao.getMerchantDataFromId(
            id
        )
    }

    override fun getMerchantDataList() = merchantDataDao.getMerchantDataList()

    override fun getMerchantDataListFromMerchantId(
        merchantId: Long,
    ) = merchantDataDao.getMerchantDataListFromMerchantId(merchantId)

    override fun searchMerchantsDataWithAllDataList(
        searchQuery: String
    ) = merchantDataDao.searchMerchantsDataWithAllDataList(searchQuery.trim())

    override fun getRecentMerchantsDataWithAllDataListFromMonth(
        timeZoneOffsetInMilli: Int,
        month: Int
    ) = merchantDataDao.getRecentMerchantsDataWithAllDataListFromMonth(
        timeZoneOffsetInMilli,
        (month + 1).toString()
    ).flowOn(dispatcher)

    override fun getRecentMerchantsDataWithAllDataListFromYear(
        timeZoneOffsetInMilli: Int,
        year: Int
    ) = merchantDataDao.getRecentMerchantsDataWithAllDataListFromYear(
        timeZoneOffsetInMilli,
        year.toString()
    ).flowOn(dispatcher)

    override fun getRecentMerchantsDataWithAllDataList() =
        merchantDataDao.getRecentMerchantsDataWithAllDataList().flowOn(dispatcher)

    override fun getMerchantsDataWithPaymentNameListFromMerchantId(
        merchantId: Long
    ) = merchantDataDao.getMerchantsDataWithPaymentNameListFromMerchantId(merchantId)

    override fun searchMerchantDataWithMerchantNameList(
        searchQuery: String, timeZoneOffsetInMilli: Int
    ) = merchantDataDao.searchMerchantDataWithMerchantNameList(
        searchQuery.trim(),
        timeZoneOffsetInMilli
    )

    override suspend fun insert(obj: MerchantData) = withContext(dispatcher) {
        merchantDataDao.insert(obj)
    }

    override suspend fun update(obj: MerchantData) = withContext(dispatcher) {
        merchantDataDao.update(obj)
    }

    /* override fun getMerchantDataWithNameWithDayTotal(timeZoneOffsetInMilli: Int) =
         merchantDataDao.getCombinedDataWithLimit(timeZoneOffsetInMilli)*/

    override fun getTotalFromMonthAsFlow(
        timeZoneOffsetInMilli: Int,
        year: Int,
        month: Int,
        toCurrencyId: Long
    ): Flow<Total> = flow {
        val baseCurrency = baseCurrencyRepository.getBaseCurrencyFromId(toCurrencyId)
            ?: BaseCurrency(
                currencySymbol = userRepository.getCurrency().first(),
                currencyCountryCode = userRepository.getCurrencyCountryCode().first()
            )

        val balanceFlow = merchantDataDao.getTotalFromMonthAsFlow(
            timeZoneOffsetInMilli,
            monthPlusOne = (month + 1).toString(),
            year = year.toString()
        ).map { list ->

            convertAmount(
                list = list,
                exchangeRateRepository = exchangeRateRepository,
                baseCurrencySymbol = baseCurrency.currencySymbol,
                baseCurrCode = baseCurrency.currencyCountryCode
            ).mergeByAmount(baseCurrency.currencyCountryCode, baseCurrency.currencySymbol)
        }
        emitAll(balanceFlow)
    }.flowOn(dispatcher)

    override suspend fun getTotalFromMonth(
        timeZoneOffsetInMilli: Int,
        year: Int,
        month: Int,
        toCurrencyId: Long
    ): Total = withContext(dispatcher) {
        val totalList = merchantDataDao.getTotalFromMonth(
            timeZoneOffsetInMilli,
            monthPlusOne = (month + 1).toString(),
            year = year.toString()
        )

        val baseCurrency = baseCurrencyRepository.getBaseCurrencyFromId(toCurrencyId)
            ?: BaseCurrency(
                currencySymbol = userRepository.getCurrency().first(),
                currencyCountryCode = userRepository.getCurrencyCountryCode().first()
            )

        convertAmount(
            list = totalList,
            baseCurrCode = baseCurrency.currencyCountryCode,
            exchangeRateRepository = exchangeRateRepository,
            baseCurrencySymbol = baseCurrency.currencySymbol
        ).mergeByAmount(baseCurrency.currencyCountryCode, baseCurrency.currencySymbol)
    }

    override fun getTotalFromYearAsFlow(
        timeZoneOffsetInMilli: Int,
        year: Int,
        toCurrencyId: Long
    ): Flow<Total> = flow {
        val baseCurrency = baseCurrencyRepository.getBaseCurrencyFromId(toCurrencyId)
            ?: BaseCurrency(
                currencySymbol = userRepository.getCurrency().first(),
                currencyCountryCode = userRepository.getCurrencyCountryCode().first()
            )

        val balanceFlow = merchantDataDao
            .getTotalFromYearAsFlow(timeZoneOffsetInMilli, year.toString())
            .map { list ->

                convertAmount(
                    list = list,
                    baseCurrencySymbol = baseCurrency.currencySymbol,
                    exchangeRateRepository = exchangeRateRepository,
                    baseCurrCode = baseCurrency.currencyCountryCode
                ).mergeByAmount(baseCurrency.currencyCountryCode, baseCurrency.currencySymbol)
            }
        emitAll(balanceFlow)
    }
        .flowOn(dispatcher)

    override suspend fun getTotalFromYear(
        timeZoneOffsetInMilli: Int,
        year: Int,
        toCurrencyId: Long
    ): Total =
        withContext(dispatcher) {
            val totalList = merchantDataDao.getTotalFromYear(timeZoneOffsetInMilli, year.toString())

            val baseCurrency = baseCurrencyRepository.getBaseCurrencyFromId(toCurrencyId)
                ?: BaseCurrency(
                    currencySymbol = userRepository.getCurrency().first(),
                    currencyCountryCode = userRepository.getCurrencyCountryCode().first()
                )

            convertAmount(
                list = totalList,
                baseCurrCode = baseCurrency.currencyCountryCode,
                exchangeRateRepository = exchangeRateRepository,
                baseCurrencySymbol = baseCurrency.currencySymbol
            ).mergeByAmount(baseCurrency.currencyCountryCode, baseCurrency.currencySymbol)
        }

    override fun getTotalAsFlow(toCurrencyId: Long): Flow<Total> =
        flow {
            val baseCurrency = baseCurrencyRepository.getBaseCurrencyFromId(toCurrencyId)
                ?: BaseCurrency(
                    currencySymbol = userRepository.getCurrency().first(),
                    currencyCountryCode = userRepository.getCurrencyCountryCode().first()
                )

            val balanceFlow = merchantDataDao
                .getTotalAsFlow()
                .map { list ->

                    convertAmount(
                        list = list,
                        baseCurrencySymbol = baseCurrency.currencySymbol,
                        exchangeRateRepository = exchangeRateRepository,
                        baseCurrCode = baseCurrency.currencyCountryCode
                    ).mergeByAmount(baseCurrency.currencyCountryCode, baseCurrency.currencySymbol)
                }
            emitAll(balanceFlow)
        }
            .flowOn(dispatcher)

    override suspend fun getTotal(toCurrencyId: Long): Total = withContext(dispatcher) {
        val totalList = merchantDataDao.getTotal()

        val baseCurrency = baseCurrencyRepository.getBaseCurrencyFromId(toCurrencyId)
            ?: BaseCurrency(
                currencySymbol = userRepository.getCurrency().first(),
                currencyCountryCode = userRepository.getCurrencyCountryCode().first()
            )
        convertAmount(
            list = totalList,
            baseCurrCode = baseCurrency.currencyCountryCode,
            exchangeRateRepository = exchangeRateRepository,
            baseCurrencySymbol = baseCurrency.currencySymbol
        ).mergeByAmount(baseCurrency.currencyCountryCode, baseCurrency.currencySymbol)
    }

    override fun getCategoryWiseExpenseFromMonthAsFlow(
        timeZoneOffsetInMilli: Int,
        year: Int,
        month: Int,
        toCurrencyId: Long
    ): Flow<List<CategoryAmount>> = flow {
        val dataList = merchantDataDao.getCategoryWiseExpenseFromMonthAsFlow(
            timeZoneOffsetInMilli = timeZoneOffsetInMilli,
            year = year.toString(),
            monthPlusOne = (month + 1).toString()
        ).map {
            val baseCurrency = baseCurrencyRepository.getBaseCurrencyFromId(toCurrencyId)
                ?: BaseCurrency(
                    currencySymbol = userRepository.getCurrency().first(),
                    currencyCountryCode = userRepository.getCurrencyCountryCode().first()
                )
            convertAmount(
                list = it,
                exchangeRateRepository = exchangeRateRepository,
                baseCurrCode = baseCurrency.currencyCountryCode,
                baseCurrencySymbol = baseCurrency.currencySymbol
            ).mergeAndSortByAmount()
        }
        emitAll(dataList)
    }
        .flowOn(dispatcher)

    override suspend fun getCategoryWiseExpenseFromMonth(
        timeZoneOffsetInMilli: Int,
        year: Int,
        month: Int,
        toCurrencyId: Long
    ): List<CategoryAmount> = withContext(dispatcher) {
        val dataList = merchantDataDao.getCategoryWiseExpenseFromMonth(
            timeZoneOffsetInMilli = timeZoneOffsetInMilli,
            year = year.toString(),
            monthPlusOne = (month + 1).toString()
        )

        val baseCurrency = baseCurrencyRepository.getBaseCurrencyFromId(toCurrencyId)
            ?: BaseCurrency(
                currencySymbol = userRepository.getCurrency().first(),
                currencyCountryCode = userRepository.getCurrencyCountryCode().first()
            )

        convertAmount(
            list = dataList,
            exchangeRateRepository = exchangeRateRepository,
            baseCurrCode = baseCurrency.currencyCountryCode,
            baseCurrencySymbol = baseCurrency.currencySymbol
        ).mergeAndSortByAmount()
    }

    override fun getCategoryWiseExpenseAsFlow(toCurrencyId: Long): Flow<List<CategoryAmount>> =
        flow {
            val dataList = merchantDataDao.getCategoryWiseExpenseAsFlow()
                .map {
                    val baseCurrency = baseCurrencyRepository.getBaseCurrencyFromId(toCurrencyId)
                        ?: BaseCurrency(
                            currencySymbol = userRepository.getCurrency().first(),
                            currencyCountryCode = userRepository.getCurrencyCountryCode().first()
                        )
                    convertAmount(
                        list = it,
                        exchangeRateRepository = exchangeRateRepository,
                        baseCurrCode = baseCurrency.currencyCountryCode,
                        baseCurrencySymbol = baseCurrency.currencySymbol
                    ).mergeAndSortByAmount()
                }
            emitAll(dataList)
        }.flowOn(dispatcher)

    override suspend fun getCategoryWiseExpense(toCurrencyId: Long): List<CategoryAmount> =
        withContext(dispatcher) {
            val dataList = merchantDataDao.getCategoryWiseExpense()

            val baseCurrency = baseCurrencyRepository.getBaseCurrencyFromId(toCurrencyId)
                ?: BaseCurrency(
                    currencySymbol = userRepository.getCurrency().first(),
                    currencyCountryCode = userRepository.getCurrencyCountryCode().first()
                )

            convertAmount(
                list = dataList,
                exchangeRateRepository = exchangeRateRepository,
                baseCurrCode = baseCurrency.currencyCountryCode,
                baseCurrencySymbol = baseCurrency.currencySymbol
            ).mergeAndSortByAmount()
        }

    override fun getCategoryWiseExpenseFromYearAsFlow(
        timeZoneOffsetInMilli: Int,
        year: Int,
        toCurrencyId: Long
    ): Flow<List<CategoryAmount>> = flow {
        val dataList = merchantDataDao
            .getCategoryWiseExpenseFromYearAsFlow(timeZoneOffsetInMilli, year.toString())
            .map {
                val baseCurrency = baseCurrencyRepository.getBaseCurrencyFromId(toCurrencyId)
                    ?: BaseCurrency(
                        currencySymbol = userRepository.getCurrency().first(),
                        currencyCountryCode = userRepository.getCurrencyCountryCode().first()
                    )
                convertAmount(
                    list = it,
                    exchangeRateRepository = exchangeRateRepository,
                    baseCurrCode = baseCurrency.currencyCountryCode,
                    baseCurrencySymbol = baseCurrency.currencySymbol
                ).mergeAndSortByAmount()
            }
        emitAll(dataList)
    }
        .flowOn(dispatcher)

    override suspend fun getCategoryWiseExpenseFromYear(
        timeZoneOffsetInMilli: Int,
        year: Int,
        toCurrencyId: Long
    ): List<CategoryAmount> = withContext(dispatcher) {
        val dataList = merchantDataDao.getCategoryWiseExpenseFromYear(
            timeZoneOffsetInMilli,
            year.toString()
        )

        val baseCurrency = baseCurrencyRepository.getBaseCurrencyFromId(toCurrencyId)
            ?: BaseCurrency(
                currencySymbol = userRepository.getCurrency().first(),
                currencyCountryCode = userRepository.getCurrencyCountryCode().first()
            )

        convertAmount(
            list = dataList,
            exchangeRateRepository = exchangeRateRepository,
            baseCurrCode = baseCurrency.currencyCountryCode,
            baseCurrencySymbol = baseCurrency.currencySymbol
        ).mergeAndSortByAmount()
    }

    override suspend fun getTotalAmountForMonthAndCategory(
        timeZoneOffsetInMilli: Int,
        year: Int,
        month: Int,
        toCurrencyId: Long,
        categoryIds: List<Long>
    ): Amount = withContext(dispatcher) {
        val dataList = merchantDataDao.getTotalAmountForMonthAndCategory(
            timeZoneOffsetInMilli = timeZoneOffsetInMilli,
            year = year.toString(),
            monthPlusOne = (month + 1).toString(),
            categoryIds = categoryIds
        )

        val baseCurrency = baseCurrencyRepository.getBaseCurrencyFromId(toCurrencyId)
            ?: BaseCurrency(
                currencySymbol = userRepository.getCurrency().first(),
                currencyCountryCode = userRepository.getCurrencyCountryCode().first()
            )

        convertAmount(
            list = dataList,
            exchangeRateRepository = exchangeRateRepository,
            baseCurrCode = baseCurrency.currencyCountryCode,
            baseCurrencySymbol = baseCurrency.currencySymbol
        ).mergeByAmount(
            baseCurrencySymbol = baseCurrency.currencySymbol,
            baseCurrCode = baseCurrency.currencyCountryCode
        )
    }

    override suspend fun getTotalAmountForYearAndCategory(
        timeZoneOffsetInMilli: Int,
        year: Int,
        toCurrencyId: Long,
        categoryIds: List<Long>
    ): Amount = withContext(dispatcher) {
        val dataList = merchantDataDao.getTotalAmountForYearAndCategory(
            timeZoneOffsetInMilli, year.toString(), categoryIds
        )
        val baseCurrency = baseCurrencyRepository.getBaseCurrencyFromId(toCurrencyId)
            ?: BaseCurrency(
                currencySymbol = userRepository.getCurrency().first(),
                currencyCountryCode = userRepository.getCurrencyCountryCode().first()
            )

        convertAmount(
            list = dataList,
            exchangeRateRepository = exchangeRateRepository,
            baseCurrCode = baseCurrency.currencyCountryCode,
            baseCurrencySymbol = baseCurrency.currencySymbol
        ).mergeByAmount(
            baseCurrencySymbol = baseCurrency.currencySymbol,
            baseCurrCode = baseCurrency.currencyCountryCode
        )
    }

    override suspend fun getTotalAmountForBetweenDatesAndCategory(
        timeZoneOffsetInMilli: Int,
        startTime: Long,
        endTime: Long,
        toCurrencyId: Long,
        categoryIds: List<Long>
    ): Amount = withContext(dispatcher) {
        val dataList = merchantDataDao.getTotalAmountForBetweenDatesAndCategory(
            startTime,
            endTime,
            categoryIds
        )
        val baseCurrency = baseCurrencyRepository.getBaseCurrencyFromId(toCurrencyId)
            ?: BaseCurrency(
                currencySymbol = userRepository.getCurrency().first(),
                currencyCountryCode = userRepository.getCurrencyCountryCode().first()
            )

        convertAmount(
            list = dataList,
            exchangeRateRepository = exchangeRateRepository,
            baseCurrCode = baseCurrency.currencyCountryCode,
            baseCurrencySymbol = baseCurrency.currencySymbol
        ).mergeByAmount(
            baseCurrencySymbol = baseCurrency.currencySymbol,
            baseCurrCode = baseCurrency.currencyCountryCode
        )
    }

    override fun getCategoryWiseTotalAmountForMonth(
        timeZoneOffsetInMilli: Int,
        year: Int,
        month: Int,
        categoryIds: List<Long>,
        toCurrencyId: Long
    ): Flow<List<CategoryAmount>> = flow {
        val baseCurrency = baseCurrencyRepository.getBaseCurrencyFromId(toCurrencyId)
            ?: BaseCurrency(
                currencySymbol = userRepository.getCurrency().first(),
                currencyCountryCode = userRepository.getCurrencyCountryCode().first()
            )
        val dataList = merchantDataDao.getCategoryWiseTotalAmountForMonth(
            timeZoneOffsetInMilli, year.toString(), (month + 1).toString(), categoryIds
        ).map {
            convertAmount(
                list = it,
                exchangeRateRepository = exchangeRateRepository,
                baseCurrCode = baseCurrency.currencyCountryCode,
                baseCurrencySymbol = baseCurrency.currencySymbol
            ).mergeAndSortByAmount()
        }
        emitAll(dataList)
    }.flowOn(dispatcher)

    override fun getCategoryWiseTotalAmountForYear(
        timeZoneOffsetInMilli: Int,
        year: Int,
        categoryIds: List<Long>,
        toCurrencyId: Long
    ): Flow<List<CategoryAmount>> = flow {
        val baseCurrency = baseCurrencyRepository.getBaseCurrencyFromId(toCurrencyId)
            ?: BaseCurrency(
                currencySymbol = userRepository.getCurrency().first(),
                currencyCountryCode = userRepository.getCurrencyCountryCode().first()
            )
        val dataList = merchantDataDao.getCategoryWiseTotalAmountForYear(
            timeZoneOffsetInMilli, year.toString(), categoryIds
        ).map {
            convertAmount(
                list = it,
                exchangeRateRepository = exchangeRateRepository,
                baseCurrCode = baseCurrency.currencyCountryCode,
                baseCurrencySymbol = baseCurrency.currencySymbol
            ).mergeAndSortByAmount()
        }
        emitAll(dataList)
    }.flowOn(dispatcher)

    override fun getCategoryWiseTotalAmountForBetweenDates(
        timeZoneOffsetInMilli: Int,
        startTime: Long,
        endTime: Long,
        categoryIds: List<Long>,
        toCurrencyId: Long
    ): Flow<List<CategoryAmount>> = flow {
        val baseCurrency = baseCurrencyRepository.getBaseCurrencyFromId(toCurrencyId)
            ?: BaseCurrency(
                currencySymbol = userRepository.getCurrency().first(),
                currencyCountryCode = userRepository.getCurrencyCountryCode().first()
            )
        val dataList = merchantDataDao.getCategoryWiseTotalAmountForBetweenDates(
            startTime,
            endTime,
            categoryIds
        ).map {
            convertAmount(
                list = it,
                exchangeRateRepository = exchangeRateRepository,
                baseCurrCode = baseCurrency.currencyCountryCode,
                baseCurrencySymbol = baseCurrency.currencySymbol
            ).mergeAndSortByAmount()
        }
        emitAll(dataList)
    }.flowOn(dispatcher)

    private suspend fun <T : ConvertibleAmount> convertAmount(
        list: List<T>,
        exchangeRateRepository: ExchangeRateRepository,
        baseCurrCode: String,
        baseCurrencySymbol: String
    ): List<T> = coroutineScope {
        val rates = exchangeRateRepository.getConversionRateFromList(list.map {
            it.toCurrencyCountry(baseCurrCode)
        })

        list.mapIndexed { index, item ->
            val rate = rates[index]
            val convertedAmounts = item.getAmounts().map { amount ->
                exchangeRateRepository.getAmountFromRate(amount, rate)
            }
            item.withConvertedAmounts(convertedAmounts, baseCurrCode, baseCurrencySymbol) as T
        }
    }

}

