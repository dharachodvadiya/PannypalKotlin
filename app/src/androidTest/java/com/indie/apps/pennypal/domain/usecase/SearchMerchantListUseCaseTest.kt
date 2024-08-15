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
class SearchMerchantListUseCaseTest {

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
    fun search_merchant_list_with_page_test() = runBlocking{
        val merchant1 = Merchant(id = 1, name = "Merchant A", dateInMilli = 5L)
        val merchant2 = Merchant(id = 2, name = "Merchant B", dateInMilli = 10L)
        val merchant3 = Merchant(id = 3, name = "ccc", dateInMilli = 10L)
        merchantDao.insert(merchant1)
        merchantDao.insert(merchant2)
        merchantDao.insert(merchant3)

        //use Paging 3
        /*val result = SearchMerchantListUseCase(merchantRepository, coroutineDispatcher).loadData("Merch",1)

        val list = result.toList()

        assert(list.size == 2)
        assert(list[0] is Resource.Loading<List<Merchant>>)
        assertNotNull(list[1])
        assert(list[1].data?.get(0)!!.name == "Merchant B")*/

    }
}