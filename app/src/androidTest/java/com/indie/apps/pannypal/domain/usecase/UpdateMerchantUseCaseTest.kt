package com.indie.apps.pannypal.domain.usecase

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.indie.apps.pannypal.data.dao.MerchantDao
import com.indie.apps.pannypal.data.db.AppDatabase
import com.indie.apps.pannypal.data.entity.Merchant
import com.indie.apps.pannypal.di.IoDispatcher
import com.indie.apps.pannypal.repository.MerchantRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class UpdateMerchantUseCaseTest {

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
    fun update_merchant_without_amount_test() = runBlocking {
        val merchant1 = Merchant(id = 1, name = "Merchant A")

        merchantDao.insert(merchant1)

        val merchant1Updated = merchant1.copy(name = "Merchant B", details = "test detail")

        val result = UpdateMerchantUseCase(
            merchantRepository = merchantRepository,
            merchant = merchant1Updated,
            dispatcher = coroutineDispatcher
        ).invoke()

        assert(result.toList().size == 2)
        assert(result.toList()[1].data == 1)

        val getMerchants = merchantDao.getMerchantList(10, 0)
        assert(getMerchants.size == 1)
        assert(getMerchants[0].name == "Merchant B")
        assert(getMerchants[0].details == "test detail")

    }

}