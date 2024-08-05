package com.indie.apps.pannypal.repository

import com.indie.apps.pannypal.data.dao.PaymentDao
import com.indie.apps.pannypal.data.entity.Payment
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(private val paymentDao: PaymentDao) :
    PaymentRepository {

    override suspend fun deleteCustomPayment(paymentId: Long) =
        paymentDao.deleteCustomPayment(paymentId)

    override suspend fun getPaymentFromId(paymentId: Long) =
        paymentDao.getPaymentFromId(paymentId)

    override fun getPaymentList() = paymentDao.getPaymentList()

    override suspend fun insertPaymentList(payments: List<Payment>) =
        paymentDao.insertPaymentList(payments)

    override fun searchPaymentList(
        searchQuery: String,
    ) = paymentDao.searchPaymentList(searchQuery)

    override suspend fun insert(payment: Payment) = paymentDao.insert(payment)

    override suspend fun update(payment: Payment) = paymentDao.update(payment)
}