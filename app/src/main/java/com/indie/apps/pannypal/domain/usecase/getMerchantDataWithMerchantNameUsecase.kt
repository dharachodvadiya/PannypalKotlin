package com.indie.apps.pannypal.domain.usecase

import android.net.http.HttpException
import androidx.compose.ui.unit.Constraints
import com.indie.apps.pannypal.data.dao.MerchantDataDao
import com.indie.apps.pannypal.data.entity.MerchantData
import com.indie.apps.pannypal.di.IoDispatcher
import com.indie.apps.pannypal.data.module.MerchantDataWithName
import com.indie.apps.pannypal.repository.MerchantDataRepository
import com.indie.apps.pannypal.util.Constant
import com.indie.apps.pannypal.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
import javax.inject.Inject

class getMerchantDataWithMerchantNameUsecase @Inject constructor(private val merchantDataRepository: MerchantDataRepository,  @IoDispatcher private val dispatcher: CoroutineDispatcher) {

    suspend fun loadData(page : Int) : Flow<Resource<List<MerchantDataWithName>>>{
        return flow{

            try {
                emit(Resource.Loading<List<MerchantDataWithName>>())
                val merchantDataWithName = merchantDataRepository.getMerchantsDataWithMerchantName(Constant.QUERY_PAGE_SIZE,Constant.QUERY_PAGE_SIZE * (page-1))
                emit(Resource.Success<List<MerchantDataWithName>>(merchantDataWithName))
            } catch(e: Throwable) {
                when(e) {
                    is IOException -> emit(Resource.Error<List<MerchantDataWithName>>("Network Failure"))
                    else -> emit(Resource.Error<List<MerchantDataWithName>>("Conversion Error"))
                }
            }
        }.flowOn(dispatcher)
    }

}