package com.indie.apps.pennypal.domain.usecase

import com.indie.apps.pennypal.data.module.YearlyTotal
import com.indie.apps.pennypal.di.IoDispatcher
import com.indie.apps.pennypal.repository.MerchantDataRepository
import com.indie.apps.pennypal.util.Util
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetTotalFromYearUseCase @Inject constructor(
    private val merchantDataRepository: MerchantDataRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {

    fun loadData(offset: Int): Flow<YearlyTotal?> {

        return merchantDataRepository.getTotalFromYear(Util.TIME_ZONE_OFFSET_IN_MILLI, offset)
            .flowOn(dispatcher)
    }

}