package com.indie.apps.pennypal.domain.usecase

import com.indie.apps.pennypal.data.module.UserWithPaymentName
import com.indie.apps.pennypal.di.IoDispatcher
import com.indie.apps.pennypal.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetUserProfileWithPaymentUseCase @Inject constructor(
    private val userRepository: UserRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {

    fun loadData(): Flow<UserWithPaymentName> {
        /* return flow {
             emit(Resource.Loading())
             try {
                 emit(Resource.Success(userRepository.getUser()))
             } catch (e: Throwable) {
                 emit(Resource.Error(handleException(e).message + ": ${e.message}"))
             }
         }
             .flowOn(dispatcher)*/
        return userRepository.getUserWithPaymentName().flowOn(dispatcher)
    }

}