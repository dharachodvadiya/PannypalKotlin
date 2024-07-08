package com.indie.apps.pannypal.domain.usecase

import android.net.http.HttpException
import androidx.compose.ui.unit.Constraints
import com.indie.apps.pannypal.data.dao.MerchantDataDao
import com.indie.apps.pannypal.data.entity.Merchant
import com.indie.apps.pannypal.data.entity.MerchantData
import com.indie.apps.pannypal.di.IoDispatcher
import com.indie.apps.pannypal.data.module.MerchantDataWithName
import com.indie.apps.pannypal.repository.MerchantDataRepository
import com.indie.apps.pannypal.repository.MerchantRepository
import com.indie.apps.pannypal.repository.UserRepository
import com.indie.apps.pannypal.util.Constant
import com.indie.apps.pannypal.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
import javax.inject.Inject

class addMerchantUsecase @Inject constructor(
    private val merchantRepository: MerchantRepository,
    private val merchant: Merchant,
    @IoDispatcher private val dispatcher: CoroutineDispatcher) {

    suspend operator fun invoke() : Flow<Resource<Long>>{
        return flow{

            try {
                emit(Resource.Loading<Long>())
                val id = merchantRepository.insert(merchant)

                if(id >0)
                {
                    emit(Resource.Success<Long>(id))
                }else{
                    emit(Resource.Error<Long>("Merchant not add"))
                }


            } catch(e: Throwable) {
                when(e) {
                    is IOException -> emit(Resource.Error<Long>("Network Failure"))
                    else -> emit(Resource.Error<Long>("Conversion Error"))
                }
            }
        }.flowOn(dispatcher)
    }

}