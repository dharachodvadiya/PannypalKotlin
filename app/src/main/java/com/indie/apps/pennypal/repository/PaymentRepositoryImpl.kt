package com.indie.apps.pennypal.repository

import com.indie.apps.pennypal.data.dao.PaymentDao
import com.indie.apps.pennypal.data.entity.Payment
import com.indie.apps.pennypal.data.entity.PaymentMode
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(private val paymentDao: PaymentDao) :
    PaymentRepository {

    override suspend fun deleteCustomPayment(paymentId: Long) =
        paymentDao.deleteCustomPayment(paymentId)

    override suspend fun getPaymentFromId(paymentId: Long) =
        paymentDao.getPaymentFromId(paymentId)

    override fun getPaymentList() = paymentDao.getPaymentList()

    override fun getPaymentListWithMode() = paymentDao.getPaymentListWithMode()

    override suspend fun insertPaymentList(payments: List<Payment>) =
        paymentDao.insertPaymentList(payments)

    override fun searchPaymentList(
        searchQuery: String,
    ) = paymentDao.searchPaymentList(searchQuery)

    override suspend fun insert(obj: Payment) = paymentDao.insert(obj)

    override suspend fun update(obj: Payment) = paymentDao.update(obj)
}