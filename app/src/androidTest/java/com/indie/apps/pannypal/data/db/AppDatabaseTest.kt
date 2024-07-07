package com.indie.apps.pannypal.data.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.indie.apps.pannypal.data.dao.PaymentDao
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppDatabaseTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var paymentDao: PaymentDao

    @Before
    fun setup() {
        val context: Context = ApplicationProvider.getApplicationContext()
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
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