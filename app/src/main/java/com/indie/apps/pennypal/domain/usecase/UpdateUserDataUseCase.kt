package com.indie.apps.pennypal.domain.usecase

import com.indie.apps.pennypal.data.entity.User
import com.indie.apps.pennypal.di.IoDispatcher
import com.indie.apps.pennypal.repository.UserRepository
import com.indie.apps.pennypal.util.Resource
import com.indie.apps.pennypal.util.handleException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UpdateUserDataUseCase @Inject constructor(
    private val userRepository: UserRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher) {

    suspend fun updateData(user: User) : Flow<Resource<Int>>{
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