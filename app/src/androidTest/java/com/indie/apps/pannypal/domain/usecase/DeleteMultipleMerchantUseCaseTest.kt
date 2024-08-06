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
class DeleteMultipleMerchantUseCaseTest {

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
    fun delete_multiple_merchant_delete_merchantDataFromMerchantId_update_userAmount_test() = runBlocking {
        val user = User(id = 1, name = "Test User", currency = "AED")
        val merchant1 = Merchant(id = 1, name = "Merchant A")
        val merchant2 = Merchant(id = 2, name = "Merchant B")
        val payment = Payment(id = 1, name = "Debit Card")

        userDao.insert(user)
        paymentDao.insert(payment)
        merchantDao.insert(merchant1)
        merchantDao.insert(merchant2)

        val merchantData1 =MerchantData(
            merchantId = merchant1.id,
            paymentId = payment.id,
            dateInMilli = System.currentTimeMillis(),
            amount = 100.0,
            type = 1
        )
        val merchantData2 =MerchantData(
            merchantId = merchant2.id,
            paymentId = payment.id,
            dateInMilli = System.currentTimeMillis(),
            amount = 10.0,
            type = -1
        )

        val result1 = AddMerchantDataUseCase(merchantDataRepository = merchantDataRepository,
            merchantRepository = merchantRepository,
            userRepository = userRepository,
            dispatcher = coroutineDispatcher).addData(merchantData1)

        val result2 = AddMerchantDataUseCase(merchantDataRepository = merchantDataRepository,
            merchantRepository = merchantRepository,
            userRepository = userRepository,
            dispatcher = coroutineDispatcher).addData(merchantData2)


        assert(result1.toList()[1].data != 0L)
        assert(result2.toList()[1].data != 0L)

        val user1 = userDao.getUser()
        user1.run {
            assert(incomeAmount == 100.0)
            assert(expenseAmount == 10.0)
        }

        val merchants = merchantDao.getMerchantList(10,0)

        assert(merchants.size == 2)

        val result3 = DeleteMultipleMerchantUseCase(
            merchantDataRepository = merchantDataRepository,
            merchantRepository = merchantRepository,
            userRepository = userRepository,
            merchants = merchants,
            dispatcher = coroutineDispatcher
        ).invoke()


        val resList = result3.toList()
        assert(resList.size == 2)
        assert(resList[1].data!! == 2)

        val merchantsData1 = merchantDataDao.getMerchantDataListFromMerchantId(merchants[0].id, 10, 0)
        val merchantsData2 = merchantDataDao.getMerchantDataListFromMerchantId(merchants[1].id, 10, 0)

        assert(merchantsData1.isEmpty())
        assert(merchantsData2.isEmpty())

        val user2 = userDao.getUser()
        user2.run {
            assert(incomeAmount == 0.0)
            assert(expenseAmount == 0.0)
        }

    }
}