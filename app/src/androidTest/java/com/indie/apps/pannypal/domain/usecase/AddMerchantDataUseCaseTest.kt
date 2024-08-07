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
class AddMerchantDataUseCaseTest {

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

    private lateinit var user: User
    private lateinit var merchant: Merchant
    private lateinit var payment: Payment

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        merchantDataDao = appDatabase.merchantDataDao()
        paymentDao = appDatabase.paymentDao()
        merchantDao = appDatabase.merchantDao()
        userDao = appDatabase.userDao()

        user = User(id = 1, name = "Test User", currency = "AED")
        merchant = Merchant(id = 1, name = "Merchant A")
        payment = Payment(id = 1, name = "Debit Card")

        runBlocking {
        userDao.insert(user)
        paymentDao.insert(payment)
        merchantDao.insert(merchant)
            }
    }

    @After
    fun tearDown() {
        runBlocking {
            appDatabase.clearAllTables()
            appDatabase.close()
        }
    }

    @Test
    fun add_single_merchantData_update_merchantAmount_update_userAmount_test() = runBlocking {

        //Arrange Data
        val merchantData = MerchantData(
            merchantId = merchant.id,
            paymentId = payment.id,
            dateInMilli = System.currentTimeMillis(),
            details = "Sample transaction",
            amount = 100.0,
            type = 1
        )

        //when
        val resultFlow = AddMerchantDataUseCase(
            merchantDataRepository = merchantDataRepository,
            merchantRepository = merchantRepository,
            userRepository = userRepository,
            dispatcher = coroutineDispatcher
        ).addData(merchantData)

        // Assert: Collect and verify the result
        resultFlow.drop(1).collect{ result ->
            when (result) {
                is Resource.Success -> assertEquals(1L, result.data)
                is Resource.Error -> fail("Unexpected Resource.Error: ${result.message}")
                is Resource.Loading -> fail("Unexpected Resource.Loading")
            }
        }

        // Assert user's updated amounts
        val getUser = userDao.getUser()
        getUser.run {
            assert(incomeAmount == 100.0)
            assert(expenseAmount == 0.0)
        }

        //use Paging 3
      /*  // Assert merchant's updated amounts
        val getMerchants = merchantDao.getMerchantList(10, 0)
        assert(getMerchants.size == 1)
        getMerchants[0].run {
            assert(getMerchants[0].incomeAmount == 100.0)
            assert(getMerchants[0].expenseAmount == 0.0)
        }*/
    }

    @Test
    fun add_two_merchantData_update_merchantAmount_update_userAmount_test() = runBlocking {

        //Arrange data
        val merchantData1 = MerchantData(
            merchantId = merchant.id,
            paymentId = payment.id,
            dateInMilli = System.currentTimeMillis(),
            details = "Sample transaction",
            amount = 100.0,
            type = 1
        )

        //when add first data
        val resultFlow1 = AddMerchantDataUseCase(
            merchantDataRepository = merchantDataRepository,
            merchantRepository = merchantRepository,
            userRepository = userRepository,
            dispatcher = coroutineDispatcher
        ).addData(merchantData1)

        // Assert: Collect and verify the result
        resultFlow1.drop(1).collect { result ->
            when (result) {
                is Resource.Success -> assertEquals(1L, result.data)
                is Resource.Error -> fail("Unexpected Resource.Error: ${result.message}")
                is Resource.Loading -> fail("Unexpected Resource.Loading")
            }
        }

        val merchantData2 = MerchantData(
            merchantId = merchant.id,
            paymentId = payment.id,
            dateInMilli = System.currentTimeMillis(),
            details = "Sample transaction",
            amount = 50.0,
            type = -1
        )

        //when add second data
        val resultFlow2 = AddMerchantDataUseCase(
            merchantDataRepository = merchantDataRepository,
            merchantRepository = merchantRepository,
            userRepository = userRepository,
            dispatcher = coroutineDispatcher
        ).addData(merchantData2)

        // Assert: Collect and verify the result
        resultFlow2.drop(1).collect { result ->
            when (result) {
                is Resource.Success -> assertEquals(2L, result.data)
                is Resource.Error -> fail("Unexpected Resource.Error: ${result.message}")
                is Resource.Loading -> fail("Unexpected Resource.Loading")
            }
        }

        // Assert user's updated amounts
        val getUser = userDao.getUser()
        getUser.run {
            assert(incomeAmount == 100.0)
            assert(expenseAmount == 50.0)
        }

        //use Paging 3
        /*// Assert merchant's updated amounts
        val getMerchants = merchantDao.getMerchantList(10, 0)
        assert(getMerchants.size == 1)
        getMerchants[0].run {
            assert(incomeAmount == 100.0)
            assert(expenseAmount == 50.0)
        }*/

    }
}