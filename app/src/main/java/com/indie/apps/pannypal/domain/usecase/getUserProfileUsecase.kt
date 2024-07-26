package com.indie.apps.pannypal.domain.usecase

import android.util.Log
import com.indie.apps.pannypal.data.entity.Payment
import com.indie.apps.pannypal.data.entity.User
import com.indie.apps.pannypal.di.IoDispatcher
import com.indie.apps.pannypal.repository.PaymentRepository
import com.indie.apps.pannypal.repository.UserRepository
import com.indie.apps.pannypal.util.Constant
import com.indie.apps.pannypal.util.Resource
import com.indie.apps.pannypal.util.handleException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
import javax.inject.Inject

class getUserProfileUsecase @Inject constructor(
    private val userRepository: UserRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher) {

    fun loadData() : Flow<Resource<User>>{
        return flow{

            try {
                emit(Resource.Loading())
                Log.d("aaaaaaa", "loadData loading")
                val merchantDataWithName = userRepository.getUser()
                Log.d("aaaaaaa", "loadData ${merchantDataWithName.name}")
                emit(Resource.Success(merchantDataWithName))
            }catch (e: Throwable) {
                Log.d("aaaaaaa", "loadData error... ${e.message}")
                emit(Resource.Error(handleException(e).message + ": ${e.message}"))
            }
        }.flowOn(dispatcher)
    }

}