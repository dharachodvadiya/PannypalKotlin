package com.indie.apps.pennypal.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.indie.apps.pennypal.data.dao.MerchantDao
import com.indie.apps.pennypal.data.dao.MerchantDataDao
import com.indie.apps.pennypal.data.dao.PaymentDao
import com.indie.apps.pennypal.data.db.AppDatabase
import com.indie.apps.pennypal.data.entity.Merchant
import com.indie.apps.pennypal.data.entity.MerchantData
import com.indie.apps.pennypal.data.entity.Payment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MerchantDataRepositoryImplTest {

    @get:Rule
    var hiltAndroidRule = HiltAndroidRule(this)

    @Inject
    lateinit var appDatabase: AppDatabase

    private lateinit var merchantDataDao: MerchantDataDao
    private lateinit var paymentDao: PaymentDao
    private lateinit var merchantDao: MerchantDao


    @Before
    fun setup() {
        hiltAndroidRule.inject()
        merchantDataDao = appDatabase.merchantDataDao()
        paymentDao = appDatabase.paymentDao()
        merchantDao = appDatabase.merchantDao()
    }

    @After
    fun teardown() {
        appDatabase.close()
    }

    @Test
    fun get_merchantsData_with_limitAndOffset_test() = runBlocking{
        // Given
        val merchant = Merchant(id = 1, name = "Merchant A")
        val payment = Payment(id = 1, name = "Debit Card")
        merchantDao.insert(merchant)
        paymentDao.insert(payment)

        // Inserting sample data
        (1..30).forEach {
            merchantDataDao.insert( MerchantData(
                merchantId = merchant.id,
                paymentId = payment.id,
                dateInMilli = System.currentTimeMillis(),
                details = "Sample transaction $it",
                amount = it.toDouble(),
                type = 1
            ))
        }

        // When
        //var merchantsData = merchantDataDao.getMerchantDataList()

       /* //Then
        assertNotNull(merchantsData)
        assertEquals(10, merchantsData.size)
        assert(30.toDouble() ==  merchantsData[0].amount)

        // When (with offset)
        merchantsData = merchantDataDao.getMerchantDataList(10,5)

        //Then
        assertNotNull(merchantsData)
        assertEquals(10, merchantsData.size)
        assert(25.toDouble() == merchantsData[0].amount)*/
    }
}