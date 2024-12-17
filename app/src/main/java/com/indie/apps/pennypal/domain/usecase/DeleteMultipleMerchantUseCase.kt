package com.indie.apps.pennypal.domain.usecase

import com.indie.apps.pennypal.di.IoDispatcher
import com.indie.apps.pennypal.repository.MerchantRepository
import com.indie.apps.pennypal.util.Resource
import com.indie.apps.pennypal.util.handleException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DeleteMultipleMerchantUseCase @Inject constructor(
    private val merchantRepository: MerchantRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {

    fun deleteData(ids: List<Long>): Flow<Resource<Int>> {
        return flow {

            try {
                emit(Resource.Loading())
                //val incomeAndExpense = merchantRepository.getTotalIncomeAndeExpenseFromIds(ids)
                val merchantDeleteCount = merchantRepository.deleteMerchantWithIdList(ids)

                if (merchantDeleteCount == ids.size) {

                    /* coroutineScope {
                         val deletedRowMerchantDataDeferred = async {
                             merchantDataRepository.deleteMerchantDataWithMerchantIdList(ids)
                         }

                         val updatedUserDeferred = async {
                             userRepository.updateAmount(-incomeAndExpense.totalIncome, -incomeAndExpense.totalExpense)
                         }

                         deletedRowMerchantDataDeferred.await()
                         updatedUserDeferred.await()

                         emit(
                             when {
                                 updatedUserDeferred.getCompleted() == 1 -> Resource.Success(merchantDeleteCount)
                                 else -> Resource.Error("Fail to update User Amount")
                             }
                         )
                     }*/

                    emit(Resource.Success(merchantDeleteCount))

                } else {
                    emit(Resource.Error("Fail to delete multiple merchant"))
                }


            } catch (e: Throwable) {
                emit(Resource.Error(handleException(e).message + ": ${e.message}"))
            }
        }.flowOn(dispatcher)
    }

}