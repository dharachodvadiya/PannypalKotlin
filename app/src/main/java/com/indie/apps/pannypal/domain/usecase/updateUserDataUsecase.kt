package com.indie.apps.pannypal.domain.usecase

import com.indie.apps.pannypal.data.entity.User
import com.indie.apps.pannypal.di.IoDispatcher
import com.indie.apps.pannypal.repository.UserRepository
import com.indie.apps.pannypal.util.Resource
import com.indie.apps.pannypal.util.handleException
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
                emit(Resource.Loading())
                val count = userRepository.update(user)

                if(count >0){
                    emit(Resource.Success(count))
                }else{
                    emit(Resource.Error("Fail to update User"))
                }


            } catch (e: Throwable) {
                emit(Resource.Error(handleException(e).message + ": ${e.message}"))
            }
        }.flowOn(dispatcher)
    }

}