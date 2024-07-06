package com.indie.apps.pannypal.repository

import com.indie.apps.pannypal.data.dao.PaymentDao
import com.indie.apps.pannypal.data.entity.Payment

class PaymentRepositoryImpl(private val paymentDao: PaymentDao) : PaymentRepository {

    override suspend fun deleteCustomPayment(paymentId: Long) = paymentDao.deleteCustomPayment(paymentId)

    override suspend fun getPayments() = paymentDao.getPayments()

    override suspend fun insert(payment: Payment) = paymentDao.insert(payment)

    override suspend fun update(payment: Payment) = paymentDao.update(payment)
}