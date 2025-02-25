package com.indie.apps.pennypal.data.database.db

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.indie.apps.cpp.data.repository.CountryRepository
import com.indie.apps.pennypal.data.database.dao.BudgetCategoryDao
import com.indie.apps.pennypal.data.database.dao.BudgetDao
import com.indie.apps.pennypal.data.database.dao.CategoryDao
import com.indie.apps.pennypal.data.database.dao.MerchantDao
import com.indie.apps.pennypal.data.database.dao.MerchantDataDao
import com.indie.apps.pennypal.data.database.dao.PaymentDao
import com.indie.apps.pennypal.data.database.dao.PaymentModeDao
import com.indie.apps.pennypal.data.database.dao.UserDao
import com.indie.apps.pennypal.data.database.entity.Budget
import com.indie.apps.pennypal.data.database.entity.BudgetCategory
import com.indie.apps.pennypal.data.database.entity.Category
import com.indie.apps.pennypal.data.database.entity.Merchant
import com.indie.apps.pennypal.data.database.entity.MerchantData
import com.indie.apps.pennypal.data.database.entity.Payment
import com.indie.apps.pennypal.data.database.entity.PaymentMode
import com.indie.apps.pennypal.data.database.entity.User
import com.indie.apps.pennypal.repository.CategoryRepositoryImpl
import com.indie.apps.pennypal.repository.PaymentModeRepositoryImpl
import com.indie.apps.pennypal.repository.PaymentRepositoryImpl
import com.indie.apps.pennypal.repository.UserRepositoryImpl
import com.indie.apps.pennypal.util.Util
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        User::class,
        Payment::class,
        Merchant::class,
        MerchantData::class,
        PaymentMode::class,
        Category::class,
        Budget::class,
        BudgetCategory::class
    ],
    version = Util.DB_VERSION,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun paymentDao(): PaymentDao
    abstract fun paymentModeDao(): PaymentModeDao
    abstract fun merchantDao(): MerchantDao
    abstract fun merchantDataDao(): MerchantDataDao
    abstract fun categoryDao(): CategoryDao
    abstract fun budgetDao(): BudgetDao
    abstract fun budgetCategoryDao(): BudgetCategoryDao

   /* fun migrateDatabaseIfNeeded(context: Context, countryRepository: CountryRepository) {

        val currentDbVersion = getCurrentDatabaseVersion(context)
        val requiredDbVersion = Util.DB_VERSION // The current version of the database schema

        if (currentDbVersion < requiredDbVersion) {
            val db = buildDatabase(context, countryRepository)
            // Triggering a migration if necessary (e.g., after restoring an old version of the DB)
            db.openHelper.writableDatabase // Accessing the database triggers migration if required
        }
    }*/

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context, countryRepository: CountryRepository): AppDatabase {
            synchronized(this) {
                return INSTANCE ?: buildDatabase(context, countryRepository).also {
                    INSTANCE = it
                }
            }
        }

        fun resetDatabaseInstance(context: Context, countryRepository: CountryRepository) {
            synchronized(this) {
                INSTANCE?.close() // Close the current instance before replacing it
                INSTANCE = buildDatabase(context, countryRepository)
                INSTANCE?.openHelper?.writableDatabase
            }
        }

        private fun buildDatabase(context: Context, countryRepository: CountryRepository): AppDatabase {
            val builder = getDbBuilder(context)
            return builder
                .addMigrations(Migration1to2(countryRepository))
                .addMigrations(Migration2to3())
                .addMigrations(Migration3to4())
                .addMigrations(Migration4to5())
                .addCallback(Callback(countryRepository))
                .build()
        }

        private fun getDbBuilder(context: Context): Builder<AppDatabase> {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                Util.DB_NAME
            )
        }

        /* private fun getCurrentDatabaseVersion(context: Context): Int {
             val db = getDbBuilder(context).build()
             return db.openHelper.readableDatabase.version
         }*/

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
                populateCategoryDb(db)

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

                UserRepositoryImpl(userDao, Dispatchers.IO).insert(user)
            }

            private suspend fun populatePaymentDb(db: AppDatabase) {
                val paymentDao = db.paymentDao()
                // Define your pre-added payment methods
                val preAddedPayments = listOf(
                    Payment(name = "Cash", preAdded = 1, modeId = 2), ////id = 1
                    Payment(name = "Bank", preAdded = 1, modeId = 3), //id = 2
                    Payment(name = "Credit Card", preAdded = 1, modeId = 4) //id = 3
                )

                PaymentRepositoryImpl(
                    paymentDao,
                    Dispatchers.IO
                ).insertPaymentList(preAddedPayments)
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

                PaymentModeRepositoryImpl(paymentModeDao, Dispatchers.IO).insertPaymentModeList(
                    preAddedPaymentMode
                )
            }

            private suspend fun populateCategoryDb(db: AppDatabase) {
                val categoryDao = db.categoryDao()
                val preAddedCategory = listOf(
                    Category(
                        name = "Other",
                        preAdded = 1,
                        type = 0,
                        iconId = 1,
                        iconColorId = 1
                    ), //id = 1
                    Category(
                        name = "Bills and Utilities",
                        preAdded = 1,
                        type = -1,
                        iconId = 2,
                        iconColorId = 2
                    ), //id = 2
                    Category(
                        name = "Education",
                        preAdded = 1,
                        type = -1,
                        iconId = 3,
                        iconColorId = 3
                    ), //id = 3
                    Category(
                        name = "Entertainment",
                        preAdded = 1,
                        type = -1,
                        iconId = 4,
                        iconColorId = 4
                    ), //id = 4
                    Category(
                        name = "Food and Dining",
                        preAdded = 1,
                        type = -1,
                        iconId = 5,
                        iconColorId = 5
                    ), //id = 5
                    Category(
                        name = "Gift and Donation",
                        preAdded = 1,
                        type = -1,
                        iconId = 6,
                        iconColorId = 6
                    ), //id = 6
                    Category(
                        name = "Insurance",
                        preAdded = 1,
                        type = -1,
                        iconId = 7,
                        iconColorId = 7
                    ), //id = 7
                    Category(
                        name = "Investments",
                        preAdded = 1,
                        type = -1,
                        iconId = 8,
                        iconColorId = 8
                    ), //id = 8
                    Category(
                        name = "Medical",
                        preAdded = 1,
                        type = -1,
                        iconId = 9,
                        iconColorId = 9
                    ), //id = 9
                    Category(
                        name = "Personal Care",
                        preAdded = 1,
                        type = -1,
                        iconId = 10,
                        iconColorId = 10
                    ), //id = 10
                    Category(
                        name = "Rent",
                        preAdded = 1,
                        type = 0,
                        iconId = 11,
                        iconColorId = 11
                    ), //id = 11
                    Category(
                        name = "Shopping",
                        preAdded = 1,
                        type = -1,
                        iconId = 12,
                        iconColorId = 12
                    ), //id = 12
                    Category(
                        name = "Taxes",
                        preAdded = 1,
                        type = -1,
                        iconId = 13,
                        iconColorId = 13
                    ), //id = 13
                    Category(
                        name = "Travelling",
                        preAdded = 1,
                        type = -1,
                        iconId = 14,
                        iconColorId = 14
                    ), //id = 14
                    Category(
                        name = "Salary",
                        preAdded = 1,
                        type = 1,
                        iconId = 15,
                        iconColorId = 15
                    ), //id = 15
                    Category(
                        name = "Rewards",
                        preAdded = 1,
                        type = 1,
                        iconId = 16,
                        iconColorId = 16
                    ), //id = 16
                )

                CategoryRepositoryImpl(categoryDao, Dispatchers.IO).insertCategoryList(
                    preAddedCategory
                )

            }


        }
    }


}