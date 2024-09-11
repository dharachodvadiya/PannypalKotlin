package com.indie.apps.pennypal.data.db

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.indie.apps.cpp.data.repository.CountryRepository
import com.indie.apps.pennypal.data.dao.MerchantDao
import com.indie.apps.pennypal.data.dao.MerchantDataDao
import com.indie.apps.pennypal.data.dao.PaymentDao
import com.indie.apps.pennypal.data.dao.PaymentModeDao
import com.indie.apps.pennypal.data.dao.UserDao
import com.indie.apps.pennypal.data.entity.Merchant
import com.indie.apps.pennypal.data.entity.MerchantData
import com.indie.apps.pennypal.data.entity.Payment
import com.indie.apps.pennypal.data.entity.PaymentMode
import com.indie.apps.pennypal.data.entity.User
import com.indie.apps.pennypal.repository.PaymentModeRepositoryImpl
import com.indie.apps.pennypal.repository.PaymentRepositoryImpl
import com.indie.apps.pennypal.repository.UserRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        User::class,
        Payment::class,
        Merchant::class,
        MerchantData::class,
        PaymentMode::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun paymentDao(): PaymentDao
    abstract fun paymentModeDao(): PaymentModeDao
    abstract fun merchantDao(): MerchantDao
    abstract fun merchantDataDao(): MerchantDataDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context, countryRepository: CountryRepository): AppDatabase {
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pennypal_money_db"
                )
                    .addMigrations(Migration1to2(countryRepository))
                    .addCallback(Callback(countryRepository))
                    .build().also {
                        INSTANCE = it
                    }
            }
        }

        private class Callback(private val countryRepository: CountryRepository) :
            RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                val scope = CoroutineScope(Dispatchers.IO)
                scope.launch {
                    INSTANCE?.let { database ->
                        // Pre-populate the database on first creation
                        populateDatabase(database)
                    }
                }
            }

            @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
            suspend fun populateDatabase(db: AppDatabase) {

                populatePaymentModeDb(db)
                populatePaymentDb(db)
                populateUserDb(db)


                /*val merchantDao = db.merchantDao()
            for (i in 1..100) {
                MerchantRepositoryImpl(merchantDao).insert(Merchant(name = "hello $i"))

            }
*/
            }

            private suspend fun populateUserDb(db: AppDatabase) {
                val user =
                    User(
                        name = "Me",
                            currency = countryRepository.getCurrencyCodeFromCountryCode(
                            countryRepository.getDefaultCountryCode(),
                        ),
                        currencyCountryCode = countryRepository.getDefaultCountryCode()
                    )
                val userDao = db.userDao()

                UserRepositoryImpl(userDao).insert(user)
            }

            private suspend fun populatePaymentDb(db: AppDatabase) {
                val paymentDao = db.paymentDao()
                // Define your pre-added payment methods
                val preAddedPayments = listOf(
                    Payment(name = "Cash", preAdded = 1, modeId = 2), ////id = 1
                    Payment(name = "Bank", preAdded = 1, modeId = 3), //id = 2
                    Payment(name = "Credit Card", preAdded = 1, modeId = 4) //id = 3
                )

                PaymentRepositoryImpl(paymentDao).insertPaymentList(preAddedPayments)
            }

            private suspend fun populatePaymentModeDb(db: AppDatabase) {
                val paymentModeDao = db.paymentModeDao()
                // Define your pre-added payment mode
                val preAddedPaymentMode = listOf(
                    PaymentMode(name = "Other"), //id = 1
                    PaymentMode(name = "Cash"),//id = 2
                    PaymentMode(name = "Bank"), //id = 3
                    PaymentMode(name = "Card"), //id = 4
                    PaymentMode(name = "Cheque"), //id = 5
                    PaymentMode(name = "Net-banking"), //id = 6
                    PaymentMode(name = "Upi") //id = 7
                )

                PaymentModeRepositoryImpl(paymentModeDao).insertPaymentModeList(preAddedPaymentMode)
            }
        }
    }
}