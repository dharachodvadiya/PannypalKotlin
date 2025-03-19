package com.indie.apps.pennypal.domain.usecase

import com.indie.apps.pennypal.data.module.category.CategoryAmount
import com.indie.apps.pennypal.di.IoDispatcher
import com.indie.apps.pennypal.repository.MerchantDataRepository
import com.indie.apps.pennypal.repository.PreferenceRepository
import com.indie.apps.pennypal.util.Resource
import com.indie.apps.pennypal.util.ShowDataPeriod
import com.indie.apps.pennypal.util.Util
import com.indie.apps.pennypal.util.handleException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetCategoryWiseExpenseUseCase @Inject constructor(
    private val merchantDataRepository: MerchantDataRepository,
    private val preferenceRepository: PreferenceRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {

    fun loadData(
        year: Int,
        month: Int,
        dataPeriod: ShowDataPeriod
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
                                month = month
                            )

                    ShowDataPeriod.THIS_YEAR ->
                        merchantDataRepository
                            .getCategoryWiseExpenseFromYear(
                                Util.TIME_ZONE_OFFSET_IN_MILLI,
                                year
                            )

                    ShowDataPeriod.ALL_TIME ->
                        merchantDataRepository
                            .getCategoryWiseExpense()

                }

                emit(Resource.Success(dataList))
            } catch (e: Throwable) {
                emit(Resource.Error(handleException(e).message + ": ${e.message}"))
            }
        }.flowOn(dispatcher)
    }

    fun loadDataAsFlow(
        year: Int,
        month: Int,
        dataPeriod: ShowDataPeriod
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
                        month = month
                    )

            ShowDataPeriod.THIS_YEAR ->
                merchantDataRepository
                    .getCategoryWiseExpenseFromYearAsFlow(
                        Util.TIME_ZONE_OFFSET_IN_MILLI,
                        year
                    )

            ShowDataPeriod.ALL_TIME ->
                merchantDataRepository
                    .getCategoryWiseExpenseAsFlow()

        }
    }

}