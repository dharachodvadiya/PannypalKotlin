package com.indie.apps.pennypal.domain.usecase

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.indie.apps.pennypal.data.dao.MerchantDao
import com.indie.apps.pennypal.data.db.AppDatabase
import com.indie.apps.pennypal.data.entity.Merchant
import com.indie.apps.pennypal.di.IoDispatcher
import com.indie.apps.pennypal.repository.MerchantRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.runBlocking

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class SearchMerchantNameAndDetailListUseCaseTest {

    @get:Rule
    var hiltAndroidRule = HiltAndroidRule(this)

    @Inject lateinit var appDatabase: AppDatabase
    @Inject lateinit var merchantRepository: MerchantRepository
    @IoDispatcher
    @Inject lateinit var coroutineDispatcher: CoroutineDispatcher

    private lateinit var merchantDao: MerchantDao

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        merchantDao = appDatabase.merchantDao()
    }

    @After
    fun tearDown() {
        appDatabase.close()
    }

    @Test
    fun search_merchant_name_details_list_with_page_test() = runBlocking{
        val merchant1 = Merchant(id = 1, name = "Merchant A", dateInMilli = 5L)
        val merchant2 = Merchant(id = 2, name = "Merchant B", dateInMilli = 10L)
        val merchant3 = Merchant(id = 3, name = "ccc", dateInMilli = 10L)
        merchantDao.insert(merchant1)
        merchantDao.insert(merchant2)
        merchantDao.insert(merchant3)

        val result = SearchMerchantNameAndDetailListUseCase(merchantRepository, coroutineDispatcher)
            .loadData("Merch")
        //ToDo Remaing testcase
    }
}