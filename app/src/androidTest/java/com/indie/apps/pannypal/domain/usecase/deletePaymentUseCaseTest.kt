package com.indie.apps.pannypal.domain.usecase

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.indie.apps.pannypal.data.dao.PaymentDao
import com.indie.apps.pannypal.data.db.AppDatabase
import com.indie.apps.pannypal.data.entity.Payment
import com.indie.apps.pannypal.di.IoDispatcher
import com.indie.apps.pannypal.repository.PaymentRepository
import com.indie.apps.pannypal.util.Resource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.drop
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
class deletePaymentUseCaseTest {

    @get:Rule
    var hiltAndroidRule = HiltAndroidRule(this)

    @Inject
    lateinit var appDatabase: AppDatabase

    @Inject
    lateinit var paymentRepository: PaymentRepository

    @IoDispatcher
    @Inject
    lateinit var coroutineDispatcher: CoroutineDispatcher

    private lateinit var paymentDao: PaymentDao

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        paymentDao = appDatabase.paymentDao()
    }

    @After
    fun tearDown() {
        appDatabase.close()
    }

    @Test
    fun delete_preAdded_payment_test() = runBlocking {
        val payment = Payment(id = 1, name = "Debit Card", 1)
        val paymentWithId  = payment.copy(id=paymentDao.insert(payment))

        val resultFlow = DeletePaymentUseCase(
            paymentRepository = paymentRepository,
            payment = paymentWithId,
            dispatcher = coroutineDispatcher
        ).invoke()

        resultFlow.drop(1).collect { result ->
            assertTrue(result is Resource.Error)
            assertEquals("Fail to delete payment", (result as Resource.Error).message)

            val getPayment = paymentDao.getPaymentList(10, 0)
            assertEquals(1, getPayment.size)
        }
    }

    @Test
    fun delete_custom_payment_test() = runBlocking {
        val payment = Payment(id = 1, name = "Debit Card")
        val paymentWithId  = payment.copy(id=paymentDao.insert(payment))

        val resultFlow = DeletePaymentUseCase(
            paymentRepository = paymentRepository,
            payment = paymentWithId,
            dispatcher = coroutineDispatcher
        ).invoke()

        resultFlow.drop(1).collect { result ->
            assertTrue(result is Resource.Success)
            assertEquals(1, (result as Resource.Success).data)

            val getPayment = paymentDao.getPaymentList(10, 0)
            assertEquals(0, getPayment.size)
        }
    }

}