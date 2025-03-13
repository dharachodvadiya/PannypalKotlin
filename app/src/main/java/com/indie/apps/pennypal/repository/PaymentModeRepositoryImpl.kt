package com.indie.apps.pennypal.repository

import com.indie.apps.pennypal.data.database.dao.PaymentModeDao
import com.indie.apps.pennypal.data.database.entity.PaymentMode
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PaymentModeRepositoryImpl @Inject constructor(
    private val paymentModeDao: PaymentModeDao,
    private val dispatcher: CoroutineDispatcher
) : PaymentModeRepository {
    override fun getPaymentModeList() = paymentModeDao.getPaymentModeList().flowOn(dispatcher)

    override suspend fun insertPaymentModeList(paymentModes: List<PaymentMode>) =
        withContext(dispatcher) {
            paymentModeDao.insertPaymentModeList(paymentModes)
        }

    override suspend fun insert(obj: PaymentMode) = withContext(dispatcher) {
        paymentModeDao.insert(
            obj
        )
    }

    override suspend fun update(obj: PaymentMode) = withContext(dispatcher) {
        paymentModeDao.update(
            obj
        )
    }


}