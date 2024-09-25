package com.indie.apps.pennypal.domain.usecase

import com.indie.apps.pennypal.data.module.category.CategoryAmount
import com.indie.apps.pennypal.data.module.category.toCategoryAmount
import com.indie.apps.pennypal.di.IoDispatcher
import com.indie.apps.pennypal.repository.MerchantDataRepository
import com.indie.apps.pennypal.repository.PreferenceRepository
import com.indie.apps.pennypal.util.ShowDataPeriod
import com.indie.apps.pennypal.util.Util
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCategoryWiseExpenseFromPreferencePeriodUseCase @Inject constructor(
    private val merchantDataRepository: MerchantDataRepository,
    private val preferenceRepository: PreferenceRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {

    fun loadData(): Flow<List<CategoryAmount>> {

        val balanceViewValue = ShowDataPeriod.fromIndex(
            preferenceRepository.getInt(
                Util.PREF_BALANCE_VIEW,
                1
            )
        )

        val categoryFlow =  when (balanceViewValue) {
            ShowDataPeriod.THIS_MONTH ->
                merchantDataRepository
                    .getCategoryWiseExpenseFromMonth(Util.TIME_ZONE_OFFSET_IN_MILLI, 0)
                    .map { it.map { item-> item.toCategoryAmount() } }

            ShowDataPeriod.THIS_YEAR ->
                merchantDataRepository.getCategoryWiseExpenseFromYear(Util.TIME_ZONE_OFFSET_IN_MILLI, 0)
                    .map { it.map { item-> item.toCategoryAmount() } }

            null ->
                merchantDataRepository
                    .getCategoryWiseExpenseFromMonth(Util.TIME_ZONE_OFFSET_IN_MILLI, 0)
                    .map { it.map { item-> item.toCategoryAmount() } }

        }

        return categoryFlow
            .flowOn(dispatcher)
    }

}