package com.indie.apps.pannypal.repository

import com.indie.apps.pannypal.data.dao.PaymentDao
import com.indie.apps.pannypal.data.entity.Payment
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(private val paymentDao: PaymentDao) : PaymentRepository {

    override suspend fun deleteCustomPayment(paymentId: Long) = paymentDao.deleteCustomPayment(paymentId)

    override suspend fun getPaymentList(limit: Int, offset: Int) = paymentDao.getPaymentList(limit, offset)

    override suspend fun insertPaymentList(payments: List<Payment>) = paymentDao.insertPaymentList(payments)

    override suspend fun searchPaymentList(
        searchQuery: String,
        limit: Int,
        offset: Int
    ) = paymentDao.searchPaymentList(searchQuery, limit, offset)

    override suspend fun insert(payment: Payment) = paymentDao.insert(payment)

    override suspend fun update(payment: Payment) = paymentDao.update(payment)
}