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
class addPaymentUseCaseTest {

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
    fun add_payment_test() = runBlocking {
        //prepare test data
        val payment = Payment(id = 1, name = "Debit Card")

        //when
        val resultFlow = AddPaymentUseCase(
            paymentRepository = paymentRepository,
            dispatcher = coroutineDispatcher
        ).addPayment(payment = payment)

        // Assert: Collect and verify the result
        resultFlow.drop(1).collect{ result ->
            when (result) {
                is Resource.Success -> assertEquals(1L, result.data)
                is Resource.Error -> fail("Unexpected Resource.Error: ${result.message}")
                is Resource.Loading -> fail("Unexpected Resource.Loading")
            }
        }

        //Assert: verify operation
        val getPayment = paymentDao.getPaymentList(10, 0)
        assert(getPayment.size == 1)
    }

}