package com.indie.apps.pennypal.repository

import com.indie.apps.pennypal.data.database.dao.PaymentDao
import com.indie.apps.pennypal.data.database.db_entity.Payment
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(
    private val paymentDao: PaymentDao,
    private val dispatcher: CoroutineDispatcher
) :
    PaymentRepository {

    override suspend fun deleteCustomPayment(paymentId: Long) = withContext(dispatcher) {
        paymentDao.softDeleteCustomPayment(paymentId)
    }

    override suspend fun getPaymentFromId(paymentId: Long) = withContext(dispatcher) {
        paymentDao.getPaymentFromId(paymentId)
    }

    override fun getPaymentList() = paymentDao.getPaymentList().flowOn(dispatcher)

    override fun getPaymentListWithMode() = paymentDao.getPaymentListWithMode().flowOn(dispatcher)

    override suspend fun insertPaymentList(payments: List<Payment>) = withContext(dispatcher) {
        paymentDao.insertPaymentList(payments)
    }

    override fun searchPaymentList(
        searchQuery: String,
    ) = paymentDao.searchPaymentList(searchQuery.trim())

    override suspend fun insert(obj: Payment) = withContext(dispatcher) {
        try {
            paymentDao.insert(obj)
        } catch (e: Exception) {
            val payments = paymentDao.getSoftDeletedPaymentFromName(obj.name)
            if (payments != null) {
                if (paymentDao.update(obj.copy(id = payments.id)) > 0)
                    payments.id
                else
                    -1
            } else
                throw Exception(e)

        }
    }

    override suspend fun update(obj: Payment) = withContext(dispatcher) { paymentDao.update(obj) }
}