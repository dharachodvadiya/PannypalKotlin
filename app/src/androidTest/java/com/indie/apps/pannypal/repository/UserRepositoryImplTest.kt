package com.indie.apps.pannypal.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.indie.apps.pannypal.data.dao.PaymentDao
import com.indie.apps.pannypal.data.dao.UserDao
import com.indie.apps.pannypal.data.db.AppDatabase
import com.indie.apps.pannypal.data.entity.User
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runBlockingTestOnTestScope
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class UserRepositoryImplTest {

    @get:Rule
    var hiltAndroidRule = HiltAndroidRule(this)

    @Inject
    lateinit var appDatabase: AppDatabase

    private lateinit var userDao: UserDao


    @Before
    fun setup() {
        hiltAndroidRule.inject()
        userDao = appDatabase.userDao()
    }

    @After
    fun teardown() {
        appDatabase.close()
    }

    @Test
    fun getUser_test() = runBlockingTestOnTestScope{
        userDao.insert(User(name = "testUser", currency = "AED"))

        val user = userDao.getUser()
        assert(user.name == "testUser")
    }


}