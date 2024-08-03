package com.indie.apps.pannypal.data.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.indie.apps.pannypal.data.dao.PaymentDao
import com.indie.apps.pannypal.data.entity.Payment
import com.indie.apps.pannypal.domain.usecase.AddMerchantUseCase
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class AppDatabaseTest {

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

    @Test
    fun populateDatabase_Test() = runBlocking {
        // Given
        val expectedPayments = listOf(
            Payment(name = "Cash"),
            Payment(name = "Bank Transfer"),
            Payment(name = "Credit Card")
        )

        //When
        AppDatabase.populateDatabase(appDatabase)

        //Then
        val result = paymentDao.getPaymentList().first()

        // Assertions
        assert(result.isNotEmpty())
        expectedPayments.forEach { expectedPayment ->
            assert(result.any { it.name == expectedPayment.name })
        }
    }

    @After
    fun teardown() {
        appDatabase.close()
    }
}