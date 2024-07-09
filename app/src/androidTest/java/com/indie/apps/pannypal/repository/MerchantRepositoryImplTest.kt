package com.indie.apps.pannypal.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.indie.apps.pannypal.data.dao.MerchantDao
import com.indie.apps.pannypal.data.dao.PaymentDao
import com.indie.apps.pannypal.data.db.AppDatabase
import com.indie.apps.pannypal.data.entity.Merchant
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
class MerchantRepositoryImplTest {

    @get:Rule
    var hiltAndroidRule = HiltAndroidRule(this)

    @Inject
    lateinit var appDatabase: AppDatabase

    private lateinit var merchantDao: MerchantDao

    @Before
    fun setup() {
        hiltAndroidRule.inject()
        merchantDao = appDatabase.merchantDao()
    }

    @After
    fun teardown() {
        appDatabase.close()
    }

    @Test
    fun getMerchants_with_limit_and_offset_test() = runBlocking{

        (1..30).forEach {
            merchantDao.insert( Merchant(name = "Item $it") )
        }

        var merchants = merchantDao.getMerchants(10, 0)
        assert(merchants.size == 10)

        merchants = merchantDao.getMerchants(10, 5)
        assert(merchants.size == 10)
    }
}