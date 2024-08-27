package com.indie.apps.pennypal.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.indie.apps.pennypal.data.dao.UserDao
import com.indie.apps.pennypal.data.db.AppDatabase
import com.indie.apps.pennypal.data.entity.User
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
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
        val user = userDao.getUser().first()

        //then
        assert(user.name == "testUser")
    }


}