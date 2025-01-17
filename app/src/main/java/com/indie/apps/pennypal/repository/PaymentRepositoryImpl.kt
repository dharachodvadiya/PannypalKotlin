package com.indie.apps.pennypal.repository

import com.indie.apps.pennypal.data.database.dao.PaymentDao
import com.indie.apps.pennypal.data.database.entity.Payment
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(
    private val paymentDao: PaymentDao,
    private val dispatcher: CoroutineDispatcher
) :
    PaymentRepository {

    override suspend fun deleteCustomPayment(paymentId: Long) =
        paymentDao.softDeleteCustomPayment(paymentId)

    override suspend fun getPaymentFromId(paymentId: Long) =
        paymentDao.getPaymentFromId(paymentId)

    override fun getPaymentList() = paymentDao.getPaymentList().flowOn(dispatcher)

    override fun getPaymentListWithMode() = paymentDao.getPaymentListWithMode().flowOn(dispatcher)

    override suspend fun insertPaymentList(payments: List<Payment>) =
        paymentDao.insertPaymentList(payments)

    override fun searchPaymentList(
        searchQuery: String,
    ) = paymentDao.searchPaymentList(searchQuery)

    override suspend fun insert(obj: Payment) = paymentDao.insert(obj)

    override suspend fun update(obj: Payment) = paymentDao.update(obj)
}