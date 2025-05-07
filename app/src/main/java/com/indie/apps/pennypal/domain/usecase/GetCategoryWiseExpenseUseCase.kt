package com.indie.apps.pennypal.domain.usecase

import com.indie.apps.pennypal.data.module.category.CategoryAmount
import com.indie.apps.pennypal.repository.MerchantDataRepository
import com.indie.apps.pennypal.util.Util
import com.indie.apps.pennypal.util.app_enum.Resource
import com.indie.apps.pennypal.util.app_enum.ShowDataPeriod
import com.indie.apps.pennypal.util.app_enum.handleException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCategoryWiseExpenseUseCase @Inject constructor(
    private val merchantDataRepository: MerchantDataRepository,
) {

    fun loadData(
        year: Int,
        month: Int,
        dataPeriod: ShowDataPeriod,
        toCurrencyId: Long,
    ): Flow<Resource<List<CategoryAmount>>> {
        return flow {

            try {
                emit(Resource.Loading())

                val dataList = when (dataPeriod) {
                    ShowDataPeriod.THIS_MONTH ->
                        merchantDataRepository
                            .getCategoryWiseExpenseFromMonth(
                                Util.TIME_ZONE_OFFSET_IN_MILLI,
                                year = year,
                                month = month,
                                toCurrencyId = toCurrencyId
                            )

                    ShowDataPeriod.THIS_YEAR ->
                        merchantDataRepository
                            .getCategoryWiseExpenseFromYear(
                                Util.TIME_ZONE_OFFSET_IN_MILLI,
                                year,
                                toCurrencyId
                            )

                    ShowDataPeriod.ALL_TIME ->
                        merchantDataRepository
                            .getCategoryWiseExpense(
                                toCurrencyId
                            )

                }

                emit(Resource.Success(dataList))
            } catch (e: Throwable) {
                emit(Resource.Error(handleException(e).message + ": ${e.message}"))
            }
        }
    }

    fun loadDataAsFlow(
        year: Int,
        month: Int,
        dataPeriod: ShowDataPeriod,
        toCurrencyId: Long,
    ): Flow<List<CategoryAmount>> {

        /*val balanceViewValue = ShowDataPeriod.fromIndex(
            preferenceRepository.getInt(
                Util.PREF_BALANCE_VIEW,
                1
            )
        )*/

        return when (dataPeriod) {
            ShowDataPeriod.THIS_MONTH ->
                merchantDataRepository
                    .getCategoryWiseExpenseFromMonthAsFlow(
                        Util.TIME_ZONE_OFFSET_IN_MILLI,
                        year = year,
                        month = month,
                        toCurrencyId = toCurrencyId
                    )

            ShowDataPeriod.THIS_YEAR ->
                merchantDataRepository
                    .getCategoryWiseExpenseFromYearAsFlow(
                        Util.TIME_ZONE_OFFSET_IN_MILLI,
                        year,
                        toCurrencyId
                    )

            ShowDataPeriod.ALL_TIME ->
                merchantDataRepository
                    .getCategoryWiseExpenseAsFlow(toCurrencyId)

        }
    }

}