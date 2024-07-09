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
class updateMerchantDataUsecaseTest {

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
    fun update_MerchantData_old_income_new_income_value_Test() = runBlocking {
        val user = User(id = 1, name = "Test User", currency = "AED")
        val merchant1 = Merchant(id = 1, name = "Merchant A")
        val payment = Payment(id = 1, name = "Debit Card")

        userDao.insert(user)
        paymentDao.insert(payment)
        merchantDao.insert(merchant1)

        val merchantData = MerchantData(
            merchantId = merchant1.id,
            paymentId = payment.id,
            dateInMilli = System.currentTimeMillis(),
            details = "Sample transaction",
            amount = 100L
        )
        val result = addMerchantDataUsecase(
            merchantDataRepository = merchantDataRepository,
            merchantRepository = merchantRepository,
            userRepository = userRepository,
            merchantData = merchantData,
            dispatcher = coroutineDispatcher
        ).invoke()

        assert(result.toList().size == 2)

        val getMerchantsData = merchantDataDao.getMerchantsData(10, 0)
        assert(getMerchantsData.size == 1)

        val newMerchant = getMerchantsData[0].copy(amount = 20)



        val result1 = updateMerchantDataUsecase(
            merchantDataRepository = merchantDataRepository,
            merchantRepository = merchantRepository,
            userRepository = userRepository,
            merchantDataOld = getMerchantsData[0],
            merchantDataNew = newMerchant,
            dispatcher = coroutineDispatcher
        ).invoke()

        val resList = result1.toList()
        assert(resList.size == 2)
        assert(resList[1].data == 1)

        val getUser = userDao.getUser()
        getUser.run {
            assert(incomeAmount == 20L)
            assert(expenseAmount == 0L)
        }

        val getMerchants = merchantDao.getMerchants(10, 0)
        assert(getMerchants.size == 1)
        getMerchants[0].run {
            assert(getMerchants[0].incomeAmount == 20L)
            assert(getMerchants[0].expenseAmount == 0L)
        }

    }

    @Test
    fun update_MerchantData_old_expense_new_expense_value_Test() = runBlocking {
        val user = User(id = 1, name = "Test User", currency = "AED")
        val merchant1 = Merchant(id = 1, name = "Merchant A")
        val payment = Payment(id = 1, name = "Debit Card")

        userDao.insert(user)
        paymentDao.insert(payment)
        merchantDao.insert(merchant1)

        val merchantData = MerchantData(
            merchantId = merchant1.id,
            paymentId = payment.id,
            dateInMilli = System.currentTimeMillis(),
            details = "Sample transaction",
            amount = -50L
        )
        val result = addMerchantDataUsecase(
            merchantDataRepository = merchantDataRepository,
            merchantRepository = merchantRepository,
            userRepository = userRepository,
            merchantData = merchantData,
            dispatcher = coroutineDispatcher
        ).invoke()

        assert(result.toList().size == 2)

        val getMerchantsData = merchantDataDao.getMerchantsData(10, 0)
        assert(getMerchantsData.size == 1)

        val newMerchant = getMerchantsData[0].copy(amount = -100L)

        val result1 = updateMerchantDataUsecase(
            merchantDataRepository = merchantDataRepository,
            merchantRepository = merchantRepository,
            userRepository = userRepository,
            merchantDataOld = getMerchantsData[0],
            merchantDataNew = newMerchant,
            dispatcher = coroutineDispatcher
        ).invoke()

        val resList = result1.toList()
        assert(resList.size == 2)
        assert(resList[1].data == 1)

        val getUser = userDao.getUser()
        getUser.run {
            assert(incomeAmount == 0L)
            assert(expenseAmount == 100L)
        }

        val getMerchants = merchantDao.getMerchants(10, 0)
        assert(getMerchants.size == 1)
        getMerchants[0].run {
            assert(getMerchants[0].incomeAmount == 0L)
            assert(getMerchants[0].expenseAmount == 100L)
        }

    }

    @Test
    fun update_MerchantData_old_expense_new_income_value_Test() = runBlocking {
        val user = User(id = 1, name = "Test User", currency = "AED")
        val merchant1 = Merchant(id = 1, name = "Merchant A")
        val payment = Payment(id = 1, name = "Debit Card")

        userDao.insert(user)
        paymentDao.insert(payment)
        merchantDao.insert(merchant1)

        val merchantData = MerchantData(
            merchantId = merchant1.id,
            paymentId = payment.id,
            dateInMilli = System.currentTimeMillis(),
            details = "Sample transaction",
            amount = -50L
        )
        val result = addMerchantDataUsecase(
            merchantDataRepository = merchantDataRepository,
            merchantRepository = merchantRepository,
            userRepository = userRepository,
            merchantData = merchantData,
            dispatcher = coroutineDispatcher
        ).invoke()

        assert(result.toList().size == 2)

        val getMerchantsData = merchantDataDao.getMerchantsData(10, 0)
        assert(getMerchantsData.size == 1)

        val newMerchant = getMerchantsData[0].copy(amount = 10)

        val result1 = updateMerchantDataUsecase(
            merchantDataRepository = merchantDataRepository,
            merchantRepository = merchantRepository,
            userRepository = userRepository,
            merchantDataOld = getMerchantsData[0],
            merchantDataNew = newMerchant,
            dispatcher = coroutineDispatcher
        ).invoke()

        val resList = result1.toList()
        assert(resList.size == 2)
        assert(resList[1].data == 1)

        val getUser = userDao.getUser()
        getUser.run {
            assert(incomeAmount == 10L)
            assert(expenseAmount == 0L)
        }

        val getMerchants = merchantDao.getMerchants(10, 0)
        assert(getMerchants.size == 1)
        getMerchants[0].run {
            assert(getMerchants[0].incomeAmount == 10L)
            assert(getMerchants[0].expenseAmount == 0L)
        }

    }

    @Test
    fun update_MerchantData_old_income_new_expense_value_Test() = runBlocking {
        val user = User(id = 1, name = "Test User", currency = "AED")
        val merchant1 = Merchant(id = 1, name = "Merchant A")
        val payment = Payment(id = 1, name = "Debit Card")

        userDao.insert(user)
        paymentDao.insert(payment)
        merchantDao.insert(merchant1)

        val merchantData = MerchantData(
            merchantId = merchant1.id,
            paymentId = payment.id,
            dateInMilli = System.currentTimeMillis(),
            details = "Sample transaction",
            amount = 70L
        )
        val result = addMerchantDataUsecase(
            merchantDataRepository = merchantDataRepository,
            merchantRepository = merchantRepository,
            userRepository = userRepository,
            merchantData = merchantData,
            dispatcher = coroutineDispatcher
        ).invoke()

        assert(result.toList().size == 2)

        val getMerchantsData = merchantDataDao.getMerchantsData(10, 0)
        assert(getMerchantsData.size == 1)

        val newMerchant = getMerchantsData[0].copy(amount = -100L)

        val result1 = updateMerchantDataUsecase(
            merchantDataRepository = merchantDataRepository,
            merchantRepository = merchantRepository,
            userRepository = userRepository,
            merchantDataOld = getMerchantsData[0],
            merchantDataNew = newMerchant,
            dispatcher = coroutineDispatcher
        ).invoke()

        val resList = result1.toList()
        assert(resList.size == 2)
        assert(resList[1].data == 1)

        val getUser = userDao.getUser()
        getUser.run {
            assert(incomeAmount == 0L)
            assert(expenseAmount == 100L)
        }

        val getMerchants = merchantDao.getMerchants(10, 0)
        assert(getMerchants.size == 1)
        getMerchants[0].run {
            assert(getMerchants[0].incomeAmount == 0L)
            assert(getMerchants[0].expenseAmount == 100L)
        }

    }

}