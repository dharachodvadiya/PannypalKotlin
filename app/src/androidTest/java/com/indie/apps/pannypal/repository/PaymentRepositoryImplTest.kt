package com.indie.apps.pannypal.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.indie.apps.pannypal.data.dao.PaymentDao
import com.indie.apps.pannypal.data.dao.UserDao
import com.indie.apps.pannypal.data.db.AppDatabase
import com.indie.apps.pannypal.data.entity.Payment
import com.indie.apps.pannypal.data.entity.User
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class PaymentRepositoryImplTest {

    @get:Rule
    var hiltAndroidRule = HiltAndroidRule(this)

    @Inject
    lateinit var appDatabase: AppDatabase

    private lateinit var paymentDao: PaymentDao


    @Before
    fun setup() {
        hiltAndroidRule.inject()
        paymentDao = appDatabase.paymentDao()
    }

    @After
    fun teardown() {
        appDatabase.close()
    }

    @Test
    fun delete_customPayment_test()= runBlocking {
        //inserting pre-added data
        AppDatabase.populateDatabase(appDatabase)

        //when [delete preadded data]
        paymentDao.deleteCustomPayment(1)

        //then
        val payments = paymentDao.getPaymentList(10,0)
        assert(payments.size == 3)

        //inserting custom data
        paymentDao.insert(Payment(name = "debitCard"))
        paymentDao.insert(Payment(name = "debitCard1"))

        //when [delete custom data]
        paymentDao.deleteCustomPayment(4)

        //then
        val payments1 = paymentDao.getPaymentList(10,0)
        assert(payments1.size == 4)
    }

    @Test
    fun get_payments_test() = runBlocking{
        //inserting custom data
        paymentDao.insert(Payment(name = "debitCard"))
        paymentDao.insert(Payment(name = "debitCard1"))

        //when
        val payments = paymentDao.getPaymentList(10, 0)

        //then
        assert(payments.isNotEmpty())
        assert(payments.size == 2) // Assuming 3 pre-added payments
        assert(payments.any { it.name == "debitCard" })
    }
}