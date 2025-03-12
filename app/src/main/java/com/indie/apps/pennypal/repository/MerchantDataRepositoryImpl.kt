package com.indie.apps.pennypal.repository

import com.indie.apps.pennypal.data.database.dao.MerchantDataDao
import com.indie.apps.pennypal.data.database.entity.MerchantData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MerchantDataRepositoryImpl @Inject constructor(
    private val merchantDataDao: MerchantDataDao,
    private val baseCurrencyRepository: BaseCurrencyRepository,
    private val userRepository: UserRepository,
    private val dispatcher: CoroutineDispatcher
) :
    MerchantDataRepository {
    override suspend fun updateMerchantDataPaymentId(oldPaymentId: Long, newPaymentId: Long) =
        merchantDataDao.updateMerchantDataPaymentId(oldPaymentId, newPaymentId)

    override suspend fun deleteMerchantDataWithId(id: Long): Int =
        merchantDataDao.deleteMerchantDataWithId(id)

    override suspend fun deleteMerchantDataWithIdList(idList: List<Long>) =
        merchantDataDao.deleteMerchantDataWithIdList(idList)

    override suspend fun deleteMerchantDataWithMerchantIdList(idList: List<Long>) =
        merchantDataDao.deleteMerchantDataWithMerchantIdList(idList)

    override suspend fun deleteMerchantDataWithMerchantId(id: Long) =
        merchantDataDao.deleteMerchantDataWithMerchantId(id)

    override suspend fun getTotalIncomeAndeExpenseFromIds(ids: List<Long>) =
        merchantDataDao.getTotalIncomeAndeExpenseFromIds(ids)

    override suspend fun getMerchantDataFromId(id: Long) = merchantDataDao.getMerchantDataFromId(id)

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
    ) = merchantDataDao.searchMerchantDataWithMerchantNameList(searchQuery.trim(), timeZoneOffsetInMilli)

    override suspend fun insert(obj: MerchantData): Long {
        val baseCurrencyId = baseCurrencyRepository.getBaseCurrencyFromCode(userRepository.getUser().first().currencyCountryCode).id
        return merchantDataDao.insert(obj.copy(baseCurrencyId = baseCurrencyId))
    }

    override suspend fun update(obj: MerchantData) = merchantDataDao.update(obj)

    override fun getMerchantDataWithNameWithDayTotal(timeZoneOffsetInMilli: Int) =
        merchantDataDao.getCombinedDataWithLimit(timeZoneOffsetInMilli)

    override fun getTotalFromMonthAsFlow(
        timeZoneOffsetInMilli: Int,
        year: Int,
        month: Int
    ) = merchantDataDao.getTotalFromMonthAsFlow(
        timeZoneOffsetInMilli,
        monthPlusOne = (month + 1).toString(),
        year = year.toString()
    ).flowOn(dispatcher)

    override suspend fun getTotalFromMonth(
        timeZoneOffsetInMilli: Int,
        year: Int,
        month: Int
    ) = merchantDataDao.getTotalFromMonth(
        timeZoneOffsetInMilli,
        monthPlusOne = (month + 1).toString(),
        year = year.toString()
    )

    override fun getTotalFromYearAsFlow(
        timeZoneOffsetInMilli: Int, year: Int
    ) = merchantDataDao.getTotalFromYearAsFlow(timeZoneOffsetInMilli, year.toString())
        .flowOn(dispatcher)

    override suspend fun getTotalFromYear(timeZoneOffsetInMilli: Int, year: Int) =
        merchantDataDao.getTotalFromYear(timeZoneOffsetInMilli, year.toString())

    override fun getTotalAsFlow() = merchantDataDao.getTotalAsFlow()

    override suspend fun getTotal() = merchantDataDao.getTotal()

    override fun getCategoryWiseExpenseFromMonthAsFlow(
        timeZoneOffsetInMilli: Int,
        year: Int,
        month: Int,
    ) = merchantDataDao.getCategoryWiseExpenseFromMonthAsFlow(
        timeZoneOffsetInMilli = timeZoneOffsetInMilli,
        year = year.toString(),
        monthPlusOne = (month + 1).toString()
    )
        .flowOn(dispatcher)

    override suspend fun getCategoryWiseExpenseFromMonth(
        timeZoneOffsetInMilli: Int,
        year: Int,
        month: Int
    ) = merchantDataDao.getCategoryWiseExpenseFromMonth(
        timeZoneOffsetInMilli = timeZoneOffsetInMilli,
        year = year.toString(),
        monthPlusOne = (month + 1).toString()
    )

    override fun getCategoryWiseExpenseAsFlow() =
        merchantDataDao.getCategoryWiseExpenseAsFlow().flowOn(dispatcher)

    override suspend fun getCategoryWiseExpense() =
        merchantDataDao.getCategoryWiseExpense()

    override fun getCategoryWiseExpenseFromYearAsFlow(
        timeZoneOffsetInMilli: Int, year: Int
    ) = merchantDataDao.getCategoryWiseExpenseFromYearAsFlow(timeZoneOffsetInMilli, year.toString())
        .flowOn(dispatcher)

    override suspend fun getCategoryWiseExpenseFromYear(
        timeZoneOffsetInMilli: Int,
        year: Int
    ) = merchantDataDao.getCategoryWiseExpenseFromYear(timeZoneOffsetInMilli, year.toString())

    override suspend fun getTotalAmountForMonthAndCategory(
        timeZoneOffsetInMilli: Int, year: Int, month: Int, categoryIds: List<Long>
    ) = merchantDataDao.getTotalAmountForMonthAndCategory(
        timeZoneOffsetInMilli = timeZoneOffsetInMilli,
        year = year.toString(),
        monthPlusOne = (month + 1).toString(),
        categoryIds = categoryIds
    )

    override suspend fun getTotalAmountForYearAndCategory(
        timeZoneOffsetInMilli: Int, year: Int, categoryIds: List<Long>
    ) = merchantDataDao.getTotalAmountForYearAndCategory(
        timeZoneOffsetInMilli, year.toString(), categoryIds
    )

    override suspend fun getTotalAmountForBetweenDatesAndCategory(
        timeZoneOffsetInMilli: Int, startTime: Long, endTime: Long, categoryIds: List<Long>
    ) = merchantDataDao.getTotalAmountForBetweenDatesAndCategory(startTime, endTime, categoryIds)

    override fun getCategoryWiseTotalAmountForMonth(
        timeZoneOffsetInMilli: Int, year: Int, month: Int, categoryIds: List<Long>
    ) = merchantDataDao.getCategoryWiseTotalAmountForMonth(
        timeZoneOffsetInMilli, year.toString(), (month + 1).toString(), categoryIds
    ).flowOn(dispatcher)

    override fun getCategoryWiseTotalAmountForYear(
        timeZoneOffsetInMilli: Int, year: Int, categoryIds: List<Long>
    ) = merchantDataDao.getCategoryWiseTotalAmountForYear(
        timeZoneOffsetInMilli, year.toString(), categoryIds
    ).flowOn(dispatcher)

    override fun getCategoryWiseTotalAmountForBetweenDates(
        timeZoneOffsetInMilli: Int, startTime: Long, endTime: Long, categoryIds: List<Long>
    ) = merchantDataDao.getCategoryWiseTotalAmountForBetweenDates(startTime, endTime, categoryIds)
        .flowOn(dispatcher)
}