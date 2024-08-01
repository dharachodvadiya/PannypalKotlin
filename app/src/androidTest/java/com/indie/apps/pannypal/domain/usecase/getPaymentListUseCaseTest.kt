package com.indie.apps.pannypal.domain.usecase

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.indie.apps.pannypal.data.dao.PaymentDao
import com.indie.apps.pannypal.data.db.AppDatabase
import com.indie.apps.pannypal.data.entity.Payment
import com.indie.apps.pannypal.di.IoDispatcher
import com.indie.apps.pannypal.repository.PaymentRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class getPaymentListUseCaseTest {

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
    fun get_payment_list_Test() = runBlocking {
        val payment1 = Payment(id = 1, name = "Debit Card")
        val payment2 = Payment(id = 2, name = "Cash")

        paymentDao.insert(payment1)
        paymentDao.insert(payment2)

        val result = GetPaymentListUseCase(
            paymentRepository = paymentRepository,
            dispatcher = coroutineDispatcher
        ).loadData(1)

        assert(result.toList().size == 2)

        val getPayment = paymentDao.getPaymentList(10, 0)
        assert(getPayment.size == 2)
    }

}