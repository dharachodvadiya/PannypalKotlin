package com.indie.apps.pannypal.domain.usecase

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.indie.apps.pannypal.data.dao.MerchantDao
import com.indie.apps.pannypal.data.dao.MerchantDataDao
import com.indie.apps.pannypal.data.dao.PaymentDao
import com.indie.apps.pannypal.data.dao.UserDao
import com.indie.apps.pannypal.data.db.AppDatabase
import com.indie.apps.pannypal.data.entity.Merchant
import com.indie.apps.pannypal.data.entity.MerchantData
import com.indie.apps.pannypal.data.entity.Payment
import com.indie.apps.pannypal.data.entity.User
import com.indie.apps.pannypal.di.IoDispatcher
import com.indie.apps.pannypal.repository.MerchantDataRepository
import com.indie.apps.pannypal.repository.MerchantRepository
import com.indie.apps.pannypal.repository.UserRepository
import com.indie.apps.pannypal.util.Resource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class DeleteMultipleMerchantDataUseCaseTest {

    @get:Rule
    var hiltAndroidRule = HiltAndroidRule(this)

    @Inject
    lateinit var appDatabase: AppDatabase

    @Inject
    lateinit var merchantDataRepository: MerchantDataRepository
    @Inject
    lateinit var merchantRepository: MerchantRepository
    @Inject
    lateinit var userRepository: UserRepository

    @IoDispatcher
    @Inject
    lateinit var coroutineDispatcher: CoroutineDispatcher

    private lateinit var merchantDataDao: MerchantDataDao
    private lateinit var paymentDao: PaymentDao
    private lateinit var merchantDao: MerchantDao
    private lateinit var userDao: UserDao

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        merchantDataDao = appDatabase.merchantDataDao()
        paymentDao = appDatabase.paymentDao()
        merchantDao = appDatabase.merchantDao()
        userDao = appDatabase.userDao()
    }

    @After
    fun tearDown() {
        appDatabase.close()
    }

    @Test
    fun delete_multiple_merchantData_update_merchantAmount_update_userAmount_test() = runBlocking {
        //prepare initial value
        val user = User(id = 1, name = "Test User", currency = "AED")
        val merchant1 = Merchant(id = 1, name = "Merchant A")
        val payment = Payment(id = 1, name = "Debit Card")

        //insert initial value
        userDao.insert(user)
        paymentDao.insert(payment)
        merchantDao.insert(merchant1)

        //prepare sample data
        val merchantData1 = MerchantData(
            merchantId = merchant1.id,
            paymentId = payment.id,
            dateInMilli = System.currentTimeMillis(),
            details = "Sample transaction",
            amount = 100.0,
            type = 1
        )

        val merchantData2 = MerchantData(
            merchantId = merchant1.id,
            paymentId = payment.id,
            dateInMilli = System.currentTimeMillis(),
            details = "Sample transaction",
            amount = 10.0,
            type = -1
        )

        val merchantData3 = MerchantData(
            merchantId = merchant1.id,
            paymentId = payment.id,
            dateInMilli = System.currentTimeMillis(),
            details = "Sample transaction",
            amount = 50.0,
            type = 1
        )

        //insert sample data
        addMerchantDataAndAssertResult(merchantData1)
        addMerchantDataAndAssertResult(merchantData2)
        addMerchantDataAndAssertResult(merchantData3)

        //use Paging 3
        /*//collect data
        val merchantDataList = merchantDataDao.getMerchantDataList(10,0)
        assert(merchantDataList.size == 3)

        //when
        val resultFlow = DeleteMultipleMerchantDataUseCase(
            merchantDataRepository = merchantDataRepository,
            merchantRepository = merchantRepository,
            userRepository = userRepository,
            merchantsData = merchantDataList,
            dispatcher = coroutineDispatcher
        ).invoke()

        // Assert: Collect and verify the result
        resultFlow.drop(1).collect{ result ->
            when (result) {
                is Resource.Success -> assertEquals(3, result.data)
                is Resource.Error -> fail("Unexpected Resource.Error: ${result.message}")
                is Resource.Loading -> fail("Unexpected Resource.Loading")
            }
        }

        //Assert: Verify Operation with user data
        val getUser = userDao.getUser()
        getUser.run {
            assert(incomeAmount == 0.0)
            assert(expenseAmount == 0.0)
        }

        //Assert: verify operation with merchant
        val getMerchants = merchantDao.getMerchantList(10, 0)
        assert(getMerchants.size == 1)
        getMerchants[0].run {
            assert(getMerchants[0].incomeAmount == 0.0)
            assert(getMerchants[0].expenseAmount == 0.0)
        }*/

    }

    private suspend fun addMerchantDataAndAssertResult(merchantData: MerchantData) {
        // Act: Add merchant data and assert the result
        val resultFlow = AddMerchantDataUseCase(
            merchantDataRepository = merchantDataRepository,
            merchantRepository = merchantRepository,
            userRepository = userRepository,
            dispatcher = coroutineDispatcher
        ).addData(merchantData)

        // Assert: Collect and verify the result
        resultFlow.drop(1).collect { result ->
            assertTrue(result is Resource.Success)
        }
    }

}