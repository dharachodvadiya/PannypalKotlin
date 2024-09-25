package com.indie.apps.pennypal.domain.usecase

import com.indie.apps.cpp.data.repository.CountryRepository
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.module.MoreItem
import com.indie.apps.pennypal.di.IoDispatcher
import com.indie.apps.pennypal.repository.PreferenceRepository
import com.indie.apps.pennypal.repository.UserRepository
import com.indie.apps.pennypal.util.ShowDataPeriod
import com.indie.apps.pennypal.util.Util
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class GetGeneralSettingUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val countryRepository: CountryRepository,
    private val preferenceRepository: PreferenceRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {

    fun loadData(): Flow<List<MoreItem>> {
        val userFlow = userRepository.getUserWithPaymentName()
        val preferenceFlow = preferenceRepository.preferenceChangeListener()

        return userFlow
            .combine(preferenceFlow.onStart { emit(Util.PREF_BALANCE_VIEW) })
            { userWithPaymentName, _ ->

                // Get the current balance view preference
                val balanceViewValue = ShowDataPeriod.fromIndex(
                    preferenceRepository.getInt(
                        Util.PREF_BALANCE_VIEW,
                        1
                    )
                )?.title

                // Create the list of MoreItem objects
                listOf(
                    MoreItem(
                        title = R.string.currency_and_format,
                        subTitle = "${userWithPaymentName.currency} (${
                            countryRepository.getSymbolFromCurrencyCode(
                                userWithPaymentName.currency
                            )
                        })"
                    ),
                    MoreItem(
                        title = R.string.default_payment_mode,
                        subTitle = userWithPaymentName.paymentName
                    ),
                    MoreItem(
                        title = R.string.home_page_balance_view,
                        subTitle = balanceViewValue
                    )
                )
            }
            .flowOn(dispatcher)
    }

}