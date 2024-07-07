package com.indie.apps.pannypal.data.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.indie.apps.pannypal.data.dao.PaymentDao
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
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

    @After
    fun teardown() {
        appDatabase.close()
    }

    @Test
    fun populateDatabase_Test() = runBlocking {
        // Perform database initialization
        AppDatabase.populateDatabase(appDatabase)

        // Verify that pre-added payments are inserted correctly
        val payments = paymentDao.getPayments()
        assert(payments.isNotEmpty())
        assert(payments.size == 3) // Assuming 3 pre-added payments
        assert(payments.any { it.name == "Cash" })
        assert(payments.any { it.name == "Bank Transfer" })
        assert(payments.any { it.name == "Credit Card" })
    }
}