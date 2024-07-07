package com.indie.apps.pannypal.data.db

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.indie.apps.pannypal.data.dao.MerchantDao
import com.indie.apps.pannypal.data.dao.MerchantDataDao
import com.indie.apps.pannypal.data.dao.PaymentDao
import com.indie.apps.pannypal.data.dao.UserDao
import com.indie.apps.pannypal.data.entity.Merchant
import com.indie.apps.pannypal.data.entity.MerchantData
import com.indie.apps.pannypal.data.entity.Payment
import com.indie.apps.pannypal.data.entity.User
import com.indie.apps.pannypal.repository.PaymentRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@Database(
    entities = [
        User::class,
        Payment::class,
        Merchant::class,
        MerchantData::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao() : UserDao
    abstract fun paymentDao() : PaymentDao
    abstract fun merchantDao() : MerchantDao
    abstract fun merchantDataDao() : MerchantDataDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pannypal_money_db"
                )
                    .addCallback(CALLBACK)
                    .build().also {
                    INSTANCE = it
                }
            }
        }

        private val CALLBACK = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                val scope = CoroutineScope(Dispatchers.IO)
                scope.launch {
                    INSTANCE?.let {database  ->
                        // Pre-populate the database on first creation
                        populateDatabase(database)
                    }
                }

            }
        }

        @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
        suspend fun populateDatabase(db: AppDatabase) {

            val paymentDao = db.paymentDao()
            // Define your pre-added payment methods
            val preAddedPayments = listOf(
                Payment(name = "Cash", preAdded = 1),
                Payment(name = "Bank Transfer", preAdded = 1),
                Payment(name = "Credit Card" , preAdded = 1)
            )

            PaymentRepositoryImpl(paymentDao).insertPayments(preAddedPayments)

        }
    }
}