package com.indie.apps.pennypal.domain.usecase

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.indie.apps.pennypal.data.dao.MerchantDao
import com.indie.apps.pennypal.data.db.AppDatabase
import com.indie.apps.pennypal.data.entity.Merchant
import com.indie.apps.pennypal.di.IoDispatcher
import com.indie.apps.pennypal.repository.MerchantRepository
import com.indie.apps.pennypal.util.Resource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class AddMerchantUseCaseTest {

    @get:Rule
    var hiltAndroidRule = HiltAndroidRule(this)

    @Inject
    lateinit var appDatabase: AppDatabase

    @Inject
    lateinit var merchantRepository: MerchantRepository

    @IoDispatcher
    @Inject
    lateinit var coroutineDispatcher: CoroutineDispatcher

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
    fun add_merchant_test() = runBlocking {
        //Arrange data
        val merchant = Merchant(id = 1, name = "Merchant A")

        //when
        val resultFlow = AddMerchantUseCase(
            merchantRepository = merchantRepository,
            dispatcher = coroutineDispatcher
        ).addMerchant(merchant = merchant)

        // Assert: Collect and verify the result
        resultFlow.drop(1).collect{ result ->
            when (result) {
                is Resource.Success -> assertEquals(1L, result.data)
                is Resource.Error -> fail("Unexpected Resource.Error: ${result.message}")
                is Resource.Loading -> fail("Unexpected Resource.Loading")
            }
        }

        //use Paging 3
       /* //Assert: Verify after operation
        val getMerchants = merchantDao.getMerchantList(10, 0)
        assert(getMerchants.size == 1)*/
    }

}