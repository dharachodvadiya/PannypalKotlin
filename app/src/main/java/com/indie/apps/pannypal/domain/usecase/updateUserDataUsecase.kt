package com.indie.apps.pannypal.domain.usecase

import com.indie.apps.pannypal.data.entity.User
import com.indie.apps.pannypal.di.IoDispatcher
import com.indie.apps.pannypal.repository.UserRepository
import com.indie.apps.pannypal.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
import javax.inject.Inject

class updateUserDataUsecase @Inject constructor(
    private val userRepository: UserRepository,
    private val user: User,
    @IoDispatcher private val dispatcher: CoroutineDispatcher) {

    suspend operator fun invoke() : Flow<Resource<Int>>{
        return flow{

            try {
                emit(Resource.Loading<Int>())
                val count = userRepository.update(user)

                if(count >0)
                {
                    emit(Resource.Success<Int>(count))
                }else{
                    emit(Resource.Error<Int>("Merchant data not updated"))
                }


            } catch(e: Throwable) {
                when(e) {
                    is IOException -> emit(Resource.Error<Int>("Network Failure"))
                    else -> emit(Resource.Error<Int>("Conversion Error"))
                }
            }
        }.flowOn(dispatcher)
    }

}