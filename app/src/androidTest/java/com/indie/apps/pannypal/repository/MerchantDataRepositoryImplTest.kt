package com.indie.apps.pannypal.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.indie.apps.pannypal.data.dao.MerchantDao
import com.indie.apps.pannypal.data.dao.MerchantDataDao
import com.indie.apps.pannypal.data.dao.PaymentDao
import com.indie.apps.pannypal.data.db.AppDatabase
import com.indie.apps.pannypal.data.entity.Merchant
import com.indie.apps.pannypal.data.entity.MerchantData
import com.indie.apps.pannypal.data.entity.Payment
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
    fun getMerchantsData_with_Limit_and_offset_test() = runBlocking{
        val merchant = Merchant(id = 1, name = "Merchant A")
        val payment = Payment(id = 1, name = "Debit Card")
        merchantDao.insert(merchant)
        paymentDao.insert(payment)

        (1..30).forEach {
            merchantDataDao.insert( MerchantData(
                merchantId = merchant.id,
                paymentId = payment.id,
                dateInMilli = System.currentTimeMillis(),
                details = "Sample transaction $it",
                amount = it.toLong()
            ))
        }

        var merchantsData = merchantDataDao.getMerchantsData(10,0)
        assertNotNull(merchantsData)
        assert(merchantsData.size == 10)
        assert(merchantsData[0].amount == 30L)

        merchantsData = merchantDataDao.getMerchantsData(10,5)
        assertNotNull(merchantsData)
        assert(merchantsData.size == 10)
        assert(merchantsData[0].amount == 25L)
    }
}