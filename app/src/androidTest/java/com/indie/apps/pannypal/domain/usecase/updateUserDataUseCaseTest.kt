package com.indie.apps.pannypal.domain.usecase

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.indie.apps.pannypal.data.dao.UserDao
import com.indie.apps.pannypal.data.db.AppDatabase
import com.indie.apps.pannypal.data.entity.User
import com.indie.apps.pannypal.di.IoDispatcher
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
class updateUserDataUseCaseTest {

    @get:Rule
    var hiltAndroidRule = HiltAndroidRule(this)

    @Inject
    lateinit var appDatabase: AppDatabase

    @Inject
    lateinit var userRepository: UserRepository

    @IoDispatcher
    @Inject
    lateinit var coroutineDispatcher: CoroutineDispatcher

    private lateinit var userDao: UserDao

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        userDao = appDatabase.userDao()
    }

    @After
    fun tearDown() {
        appDatabase.close()
    }

    @Test
    fun update_user_without_amount_test() = runBlocking {
        val user = User(id = 1, name = "Test User", currency = "AED")

        userDao.insert(user)

        val userWithEmail = user.copy(email = "test@gmail.com")

        val result = UpdateUserDataUseCase(
            userRepository = userRepository,
            userWithEmail,
            dispatcher = coroutineDispatcher
        ).invoke()

        assert(result.toList().size == 2)

        val getUser = userDao.getUser()
        getUser.run {
            assert(getUser.email == "test@gmail.com")
        }
    }

}