package com.indie.apps.pannypal.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.indie.apps.pannypal.data.dao.PaymentDao
import com.indie.apps.pannypal.data.dao.UserDao
import com.indie.apps.pannypal.data.db.AppDatabase
import com.indie.apps.pannypal.data.entity.User
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
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
    fun get_user_test() = runBlocking{
        //inserting sample data
        userDao.insert(User(name = "testUser", currency = "AED"))

        //when
        val user = userDao.getUser()

        //then
        assert(user.name == "testUser")
    }


}