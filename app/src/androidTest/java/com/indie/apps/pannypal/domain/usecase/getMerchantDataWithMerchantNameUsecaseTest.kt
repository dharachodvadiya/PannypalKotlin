package com.indie.apps.pannypal.domain.usecase

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.indie.apps.pannypal.data.dao.MerchantDao
import com.indie.apps.pannypal.data.dao.MerchantDataDao
import com.indie.apps.pannypal.data.dao.PaymentDao
import com.indie.apps.pannypal.data.db.AppDatabase
import com.indie.apps.pannypal.data.entity.Merchant
import com.indie.apps.pannypal.data.entity.MerchantData
import com.indie.apps.pannypal.data.entity.Payment
import com.indie.apps.pannypal.data.module.MerchantDataWithName
import com.indie.apps.pannypal.di.IoDispatcher
import com.indie.apps.pannypal.repository.MerchantDataRepository
import com.indie.apps.pannypal.repository.MerchantRepository
import com.indie.apps.pannypal.util.Constant
import com.indie.apps.pannypal.util.Resource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
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
class getMerchantDataWithMerchantNameUsecaseTest {

    @get:Rule
    var hiltAndroidRule = HiltAndroidRule(this)

    @Inject lateinit var appDatabase: AppDatabase
    @Inject lateinit var merchantDataRepository: MerchantDataRepository
    @IoDispatcher
    @Inject lateinit var coroutineDispatcher: CoroutineDispatcher

    private lateinit var merchantDataDao: MerchantDataDao
    private lateinit var paymentDao: PaymentDao
    private lateinit var merchantDao: MerchantDao

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        merchantDataDao = appDatabase.merchantDataDao()
        paymentDao = appDatabase.paymentDao()
        merchantDao = appDatabase.merchantDao()
    }

    @After
    fun tearDown() {
        appDatabase.close()
    }

    @Test
    fun getMerchantsData_with_merchantName_with_Limit_and_offset_test() = runBlocking{
        val merchant1 = Merchant(id = 1, name = "Merchant A")
        val merchant2 = Merchant(id = 2, name = "Merchant B")
        val payment = Payment(id = 1, name = "Debit Card")
        merchantDao.insert(merchant1)
        merchantDao.insert(merchant2)
        paymentDao.insert(payment)

        (1..30).forEach {
            merchantDataDao.insert( MerchantData(
                merchantId = if (it % 2 == 0) merchant2.id else merchant1.id,
                paymentId = payment.id,
                dateInMilli = System.currentTimeMillis(),
                details = "Sample transaction $it",
                amount = it.toLong()
            )
            )
        }

        val result = getMerchantDataWithMerchantNameUsecase(merchantDataRepository, coroutineDispatcher).loadData(1)

        val list = result.toList()


        assert(list.size == 2)
        assert(list[0] is Resource.Loading<List<MerchantDataWithName>>)
        assertNotNull(list[1].data)
        assert(list[1].data!!.size == Constant.QUERY_PAGE_SIZE)

    }
}