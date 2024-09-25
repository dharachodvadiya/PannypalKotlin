package com.indie.apps.pennypal.repository

import com.indie.apps.pennypal.data.database.dao.PaymentModeDao
import com.indie.apps.pennypal.data.database.entity.PaymentMode
import javax.inject.Inject

class PaymentModeRepositoryImpl @Inject constructor(private val paymentModeDao: PaymentModeDao) :
    PaymentModeRepository {
    override fun getPaymentModeList() = paymentModeDao.getPaymentModeList()

    override suspend fun insertPaymentModeList(paymentModes: List<PaymentMode>) =
        paymentModeDao.insertPaymentModeList(paymentModes)

    override suspend fun insert(obj: PaymentMode) = paymentModeDao.insert(obj)

    override suspend fun update(obj: PaymentMode) = paymentModeDao.update(obj)


}