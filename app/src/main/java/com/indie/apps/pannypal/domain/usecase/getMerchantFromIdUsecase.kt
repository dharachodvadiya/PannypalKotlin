package com.indie.apps.pannypal.domain.usecase

import android.net.http.HttpException
import androidx.compose.ui.unit.Constraints
import com.indie.apps.pannypal.data.dao.MerchantDataDao
import com.indie.apps.pannypal.data.entity.Merchant
import com.indie.apps.pannypal.data.entity.MerchantData
import com.indie.apps.pannypal.di.IoDispatcher
import com.indie.apps.pannypal.data.module.MerchantDataWithName
import com.indie.apps.pannypal.data.module.MerchantNameAndDetails
import com.indie.apps.pannypal.repository.MerchantDataRepository
import com.indie.apps.pannypal.repository.MerchantRepository
import com.indie.apps.pannypal.util.Constant
import com.indie.apps.pannypal.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
import javax.inject.Inject

class getMerchantFromIdUsecase @Inject constructor(
    private val merchantRepository: MerchantRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher) {

    suspend operator fun invoke(id: Long) : Flow<Resource<Merchant>>{
        return flow{

            try {
                emit(Resource.Loading<Merchant>())
                val merchantFromId = merchantRepository.getMerchantFromId(id)
                emit(Resource.Success<Merchant>(merchantFromId))
            } catch(e: Throwable) {
                when(e) {
                    is IOException -> emit(Resource.Error<Merchant>("Network Failure"))
                    else -> emit(Resource.Error<Merchant>("Conversion Error"))
                }
            }
        }.flowOn(dispatcher)
    }

}