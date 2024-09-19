package com.indie.apps.pennypal.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.indie.apps.pennypal.data.module.DailyTotal
import com.indie.apps.pennypal.data.module.MonthlyTotal
import com.indie.apps.pennypal.di.IoDispatcher
import com.indie.apps.pennypal.util.Util
import com.indie.apps.pennypal.repository.MerchantDataRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetMonthlyTotalUseCase @Inject constructor(
    private val merchantDataRepository: MerchantDataRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {

    fun loadData(offset: Int): Flow<List<MonthlyTotal>> {

        return merchantDataRepository.getMonthlyTotalList(Util.TIME_ZONE_OFFSET_IN_MILLI, offset)
            .flowOn(dispatcher)
    }

}