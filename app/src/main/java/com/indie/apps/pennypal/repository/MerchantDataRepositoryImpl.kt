package com.indie.apps.pennypal.repository

import com.indie.apps.pennypal.data.database.dao.MerchantDataDao
import com.indie.apps.pennypal.data.database.entity.MerchantData
import com.indie.apps.pennypal.data.module._interface.ConvertibleAmount
import com.indie.apps.pennypal.data.module.balance.mergeByAmount
import com.indie.apps.pennypal.data.module.category.mergeAndSortByAmount
import com.indie.apps.pennypal.data.module.mergeByAmount
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.coroutineScope
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
        month: Int
    ) = flow {
        val baseCurrCode = userRepository.getCurrencyCountryCode().first()
        val baseCurrencySymbol = userRepository.getCurrency().first()

        val balanceFlow = merchantDataDao.getTotalFromMonthAsFlow(
            timeZoneOffsetInMilli,
            monthPlusOne = (month + 1).toString(),
            year = year.toString()
        ).map { list ->

            convertAmount(
                list = list,
                exchangeRateRepository = exchangeRateRepository,
                baseCurrencySymbol = baseCurrencySymbol,
                baseCurrCode = baseCurrCode
            ).mergeByAmount(baseCurrCode, baseCurrencySymbol)
        }
        emitAll(balanceFlow)
    }.flowOn(dispatcher)

    override suspend fun getTotalFromMonth(
        timeZoneOffsetInMilli: Int,
        year: Int,
        month: Int
    ) = withContext(dispatcher) {
        val totalList = merchantDataDao.getTotalFromMonth(
            timeZoneOffsetInMilli,
            monthPlusOne = (month + 1).toString(),
            year = year.toString()
        )

        val baseCurrCode = userRepository.getCurrencyCountryCode().first()
        val baseCurrencySymbol = userRepository.getCurrency().first()

        convertAmount(
            list = totalList,
            baseCurrCode = baseCurrCode,
            exchangeRateRepository = exchangeRateRepository,
            baseCurrencySymbol = baseCurrencySymbol
        ).mergeByAmount(baseCurrCode, baseCurrencySymbol)
    }

    override fun getTotalFromYearAsFlow(
        timeZoneOffsetInMilli: Int, year: Int
    ) = flow {
        val baseCurrCode = userRepository.getCurrencyCountryCode().first()
        val baseCurrencySymbol = userRepository.getCurrency().first()

        val balanceFlow = merchantDataDao
            .getTotalFromYearAsFlow(timeZoneOffsetInMilli, year.toString())
            .map { list ->

                convertAmount(
                    list = list,
                    baseCurrencySymbol = baseCurrencySymbol,
                    exchangeRateRepository = exchangeRateRepository,
                    baseCurrCode = baseCurrCode
                ).mergeByAmount(baseCurrCode, baseCurrencySymbol)
            }
        emitAll(balanceFlow)
    }
        .flowOn(dispatcher)

    override suspend fun getTotalFromYear(timeZoneOffsetInMilli: Int, year: Int) =
        withContext(dispatcher) {
            val totalList = merchantDataDao.getTotalFromYear(timeZoneOffsetInMilli, year.toString())

            val baseCurrCode = userRepository.getCurrencyCountryCode().first()
            val baseCurrencySymbol = userRepository.getCurrency().first()

            convertAmount(
                list = totalList,
                baseCurrCode = baseCurrCode,
                exchangeRateRepository = exchangeRateRepository,
                baseCurrencySymbol = baseCurrencySymbol
            ).mergeByAmount(baseCurrCode, baseCurrencySymbol)
        }

    override fun getTotalAsFlow() =
        flow {
            val baseCurrCode = userRepository.getCurrencyCountryCode().first()
            val baseCurrencySymbol = userRepository.getCurrency().first()

            val balanceFlow = merchantDataDao
                .getTotalAsFlow()
                .map { list ->

                    convertAmount(
                        list = list,
                        baseCurrencySymbol = baseCurrencySymbol,
                        exchangeRateRepository = exchangeRateRepository,
                        baseCurrCode = baseCurrCode
                    ).mergeByAmount(baseCurrCode, baseCurrencySymbol)
                }
            emitAll(balanceFlow)
        }
            .flowOn(dispatcher)

    override suspend fun getTotal() = withContext(dispatcher) {
        val totalList = merchantDataDao.getTotal()

        val baseCurrCode = userRepository.getCurrencyCountryCode().first()
        val baseCurrencySymbol = userRepository.getCurrency().first()
        convertAmount(
            list = totalList,
            baseCurrCode = baseCurrCode,
            exchangeRateRepository = exchangeRateRepository,
            baseCurrencySymbol = baseCurrencySymbol
        ).mergeByAmount(baseCurrCode, baseCurrencySymbol)
    }

    override fun getCategoryWiseExpenseFromMonthAsFlow(
        timeZoneOffsetInMilli: Int,
        year: Int,
        month: Int,
    ) = flow {
        val dataList = merchantDataDao.getCategoryWiseExpenseFromMonthAsFlow(
            timeZoneOffsetInMilli = timeZoneOffsetInMilli,
            year = year.toString(),
            monthPlusOne = (month + 1).toString()
        ).map {
            val baseCurrCode = userRepository.getCurrencyCountryCode().first()
            val baseCurrencySymbol = userRepository.getCurrency().first()
            convertAmount(
                list = it,
                exchangeRateRepository = exchangeRateRepository,
                baseCurrCode = baseCurrCode,
                baseCurrencySymbol = baseCurrencySymbol
            ).mergeAndSortByAmount()
        }
        emitAll(dataList)
    }
        .flowOn(dispatcher)

    override suspend fun getCategoryWiseExpenseFromMonth(
        timeZoneOffsetInMilli: Int,
        year: Int,
        month: Int
    ) = withContext(dispatcher) {
        val dataList = merchantDataDao.getCategoryWiseExpenseFromMonth(
            timeZoneOffsetInMilli = timeZoneOffsetInMilli,
            year = year.toString(),
            monthPlusOne = (month + 1).toString()
        )

        val baseCurrCode = userRepository.getCurrencyCountryCode().first()
        val baseCurrencySymbol = userRepository.getCurrency().first()

        convertAmount(
            list = dataList,
            exchangeRateRepository = exchangeRateRepository,
            baseCurrCode = baseCurrCode,
            baseCurrencySymbol = baseCurrencySymbol
        ).mergeAndSortByAmount()
    }

    override fun getCategoryWiseExpenseAsFlow() =
        flow {
            val dataList = merchantDataDao.getCategoryWiseExpenseAsFlow()
                .map {
                    val baseCurrCode = userRepository.getCurrencyCountryCode().first()
                    val baseCurrencySymbol = userRepository.getCurrency().first()
                    convertAmount(
                        list = it,
                        exchangeRateRepository = exchangeRateRepository,
                        baseCurrCode = baseCurrCode,
                        baseCurrencySymbol = baseCurrencySymbol
                    ).mergeAndSortByAmount()
                }
            emitAll(dataList)
        }.flowOn(dispatcher)

    override suspend fun getCategoryWiseExpense() = withContext(dispatcher) {
        val dataList = merchantDataDao.getCategoryWiseExpense()

        val baseCurrCode = userRepository.getCurrencyCountryCode().first()
        val baseCurrencySymbol = userRepository.getCurrency().first()

        convertAmount(
            list = dataList,
            exchangeRateRepository = exchangeRateRepository,
            baseCurrCode = baseCurrCode,
            baseCurrencySymbol = baseCurrencySymbol
        ).mergeAndSortByAmount()
    }

    override fun getCategoryWiseExpenseFromYearAsFlow(
        timeZoneOffsetInMilli: Int, year: Int
    ) = flow {
        val dataList = merchantDataDao
            .getCategoryWiseExpenseFromYearAsFlow(timeZoneOffsetInMilli, year.toString())
            .map {
                val baseCurrCode = userRepository.getCurrencyCountryCode().first()
                val baseCurrencySymbol = userRepository.getCurrency().first()
                convertAmount(
                    list = it,
                    exchangeRateRepository = exchangeRateRepository,
                    baseCurrCode = baseCurrCode,
                    baseCurrencySymbol = baseCurrencySymbol
                ).mergeAndSortByAmount()
            }
        emitAll(dataList)
    }
        .flowOn(dispatcher)

    override suspend fun getCategoryWiseExpenseFromYear(
        timeZoneOffsetInMilli: Int,
        year: Int
    ) = withContext(dispatcher) {
        val dataList = merchantDataDao.getCategoryWiseExpenseFromYear(
            timeZoneOffsetInMilli,
            year.toString()
        )

        val baseCurrCode = userRepository.getCurrencyCountryCode().first()
        val baseCurrencySymbol = userRepository.getCurrency().first()

        convertAmount(
            list = dataList,
            exchangeRateRepository = exchangeRateRepository,
            baseCurrCode = baseCurrCode,
            baseCurrencySymbol = baseCurrencySymbol
        ).mergeAndSortByAmount()
    }

    override suspend fun getTotalAmountForMonthAndCategory(
        timeZoneOffsetInMilli: Int, year: Int, month: Int, categoryIds: List<Long>
    ) = withContext(dispatcher) {
        val dataList = merchantDataDao.getTotalAmountForMonthAndCategory(
            timeZoneOffsetInMilli = timeZoneOffsetInMilli,
            year = year.toString(),
            monthPlusOne = (month + 1).toString(),
            categoryIds = categoryIds
        )

        val baseCurrCode = userRepository.getCurrencyCountryCode().first()
        val baseCurrencySymbol = userRepository.getCurrency().first()

        convertAmount(
            list = dataList,
            exchangeRateRepository = exchangeRateRepository,
            baseCurrCode = baseCurrCode,
            baseCurrencySymbol = baseCurrencySymbol
        ).mergeByAmount(baseCurrCode, baseCurrencySymbol)
    }

    override suspend fun getTotalAmountForYearAndCategory(
        timeZoneOffsetInMilli: Int, year: Int, categoryIds: List<Long>
    ) = withContext(dispatcher) {
        val dataList = merchantDataDao.getTotalAmountForYearAndCategory(
            timeZoneOffsetInMilli, year.toString(), categoryIds
        )
        val baseCurrCode = userRepository.getCurrencyCountryCode().first()
        val baseCurrencySymbol = userRepository.getCurrency().first()

        convertAmount(
            list = dataList,
            exchangeRateRepository = exchangeRateRepository,
            baseCurrCode = baseCurrCode,
            baseCurrencySymbol
        ).mergeByAmount(baseCurrCode, baseCurrencySymbol)
    }

    override suspend fun getTotalAmountForBetweenDatesAndCategory(
        timeZoneOffsetInMilli: Int, startTime: Long, endTime: Long, categoryIds: List<Long>
    ) = withContext(dispatcher) {
        val dataList = merchantDataDao.getTotalAmountForBetweenDatesAndCategory(
            startTime,
            endTime,
            categoryIds
        )
        val baseCurrCode = userRepository.getCurrencyCountryCode().first()
        val baseCurrencySymbol = userRepository.getCurrency().first()

        convertAmount(
            list = dataList,
            exchangeRateRepository = exchangeRateRepository,
            baseCurrCode = baseCurrCode,
            baseCurrencySymbol
        ).mergeByAmount(baseCurrCode, baseCurrencySymbol)
    }

    override fun getCategoryWiseTotalAmountForMonth(
        timeZoneOffsetInMilli: Int, year: Int, month: Int, categoryIds: List<Long>
    ) = flow {
        val baseCurrCode = userRepository.getCurrencyCountryCode().first()
        val baseCurrencySymbol = userRepository.getCurrency().first()
        val dataList = merchantDataDao.getCategoryWiseTotalAmountForMonth(
            timeZoneOffsetInMilli, year.toString(), (month + 1).toString(), categoryIds
        ).map {
            convertAmount(
                list = it,
                exchangeRateRepository = exchangeRateRepository,
                baseCurrCode = baseCurrCode,
                baseCurrencySymbol = baseCurrencySymbol
            ).mergeAndSortByAmount()
        }
        emitAll(dataList)
    }.flowOn(dispatcher)

    override fun getCategoryWiseTotalAmountForYear(
        timeZoneOffsetInMilli: Int, year: Int, categoryIds: List<Long>
    ) = flow {
        val baseCurrCode = userRepository.getCurrencyCountryCode().first()
        val baseCurrencySymbol = userRepository.getCurrency().first()
        val dataList = merchantDataDao.getCategoryWiseTotalAmountForYear(
            timeZoneOffsetInMilli, year.toString(), categoryIds
        ).map {
            convertAmount(
                list = it,
                exchangeRateRepository = exchangeRateRepository,
                baseCurrCode = baseCurrCode,
                baseCurrencySymbol = baseCurrencySymbol
            ).mergeAndSortByAmount()
        }
        emitAll(dataList)
    }.flowOn(dispatcher)

    override fun getCategoryWiseTotalAmountForBetweenDates(
        timeZoneOffsetInMilli: Int, startTime: Long, endTime: Long, categoryIds: List<Long>
    ) = flow {
        val baseCurrCode = userRepository.getCurrencyCountryCode().first()
        val baseCurrencySymbol = userRepository.getCurrency().first()
        val dataList = merchantDataDao.getCategoryWiseTotalAmountForBetweenDates(
            startTime,
            endTime,
            categoryIds
        ).map {
            convertAmount(
                list = it,
                exchangeRateRepository = exchangeRateRepository,
                baseCurrCode = baseCurrCode,
                baseCurrencySymbol = baseCurrencySymbol
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

