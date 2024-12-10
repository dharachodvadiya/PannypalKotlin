package com.indie.apps.pennypal.repository

import com.indie.apps.pennypal.data.database.dao.MerchantDataDao
import com.indie.apps.pennypal.data.database.entity.MerchantData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MerchantDataRepositoryImpl @Inject constructor(
    private val merchantDataDao: MerchantDataDao,
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
    ) = merchantDataDao.searchMerchantsDataWithAllDataList(searchQuery)

    override fun getRecentMerchantsDataWithAllDataList() =
        merchantDataDao.getRecentMerchantsDataWithAllDataList().flowOn(dispatcher)

    override fun getMerchantsDataWithPaymentNameListFromMerchantId(
        merchantId: Long
    ) = merchantDataDao.getMerchantsDataWithPaymentNameListFromMerchantId(merchantId)

    override fun searchMerchantDataWithMerchantNameList(
        searchQuery: String, timeZoneOffsetInMilli: Int
    ) = merchantDataDao.searchMerchantDataWithMerchantNameList(searchQuery, timeZoneOffsetInMilli)

    override suspend fun insert(obj: MerchantData) = merchantDataDao.insert(obj)

    override suspend fun update(obj: MerchantData) = merchantDataDao.update(obj)

    override fun getMerchantDataWithNameWithDayTotal(timeZoneOffsetInMilli: Int) =
        merchantDataDao.getCombinedDataWithLimit(timeZoneOffsetInMilli)

    override fun getTotalFromMonth(
        timeZoneOffsetInMilli: Int, monthOffset: Int
    ) = merchantDataDao.getTotalFromMonth(timeZoneOffsetInMilli, monthOffset).flowOn(dispatcher)


    override fun getTotalFromYear(
        timeZoneOffsetInMilli: Int, offset: Int
    ) = merchantDataDao.getTotalFromYear(timeZoneOffsetInMilli, offset).flowOn(dispatcher)

    override fun getCategoryWiseExpenseFromMonth(
        timeZoneOffsetInMilli: Int, monthOffset: Int
    ) = merchantDataDao.getCategoryWiseExpenseFromMonth(timeZoneOffsetInMilli, monthOffset)
        .flowOn(dispatcher)

    override fun getCategoryWiseExpenseFromYear(
        timeZoneOffsetInMilli: Int, yearOffset: Int
    ) = merchantDataDao.getCategoryWiseExpenseFromYear(timeZoneOffsetInMilli, yearOffset)
        .flowOn(dispatcher)

    override suspend fun getTotalAmountForMonthAndCategory(
        timeZoneOffsetInMilli: Int, year: Int, monthPlusOne: Int, categoryIds: List<Long>
    ) = merchantDataDao.getTotalAmountForMonthAndCategory(
        timeZoneOffsetInMilli = timeZoneOffsetInMilli,
        year = year.toString(),
        monthPlusOne = monthPlusOne.toString(),
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
        timeZoneOffsetInMilli: Int, year: Int, monthPlusOne: Int, categoryIds: List<Long>
    ) = merchantDataDao.getCategoryWiseTotalAmountForMonth(
        timeZoneOffsetInMilli, year.toString(), monthPlusOne.toString(), categoryIds
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