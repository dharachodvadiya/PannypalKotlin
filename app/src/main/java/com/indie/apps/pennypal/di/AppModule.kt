package com.indie.apps.pennypal.di

import android.content.Context
import com.indie.apps.contacts.data.provider.ContactsProvider
import com.indie.apps.contacts.data.provider.impl.ContactsProviderImpl
import com.indie.apps.contacts.data.repo.ContactsRepository
import com.indie.apps.contacts.data.repo.impl.ContactsRepositoryImpl
import com.indie.apps.cpp.data.CountryDb
import com.indie.apps.cpp.data.repository.CountryRepository
import com.indie.apps.cpp.data.repository.CountryRepositoryImpl
import com.indie.apps.pennypal.data.database.db.AppDatabase
import com.indie.apps.pennypal.data.preference.PreferenceManager
import com.indie.apps.pennypal.data.service.ExchangeRateApiService
import com.indie.apps.pennypal.data.service.NetworkMonitor
import com.indie.apps.pennypal.repository.AuthRepository
import com.indie.apps.pennypal.repository.AuthRepositoryImpl
import com.indie.apps.pennypal.repository.BackupRepository
import com.indie.apps.pennypal.repository.BackupRepositoryImpl
import com.indie.apps.pennypal.repository.BaseCurrencyRepository
import com.indie.apps.pennypal.repository.BaseCurrencyRepositoryImpl
import com.indie.apps.pennypal.repository.BillingRepository
import com.indie.apps.pennypal.repository.BillingRepositoryImpl
import com.indie.apps.pennypal.repository.BudgetRepository
import com.indie.apps.pennypal.repository.BudgetRepositoryImpl
import com.indie.apps.pennypal.repository.CategoryRepository
import com.indie.apps.pennypal.repository.CategoryRepositoryImpl
import com.indie.apps.pennypal.repository.ExchangeRateRepository
import com.indie.apps.pennypal.repository.ExchangeRateRepositoryImpl
import com.indie.apps.pennypal.repository.MerchantDataRepository
import com.indie.apps.pennypal.repository.MerchantDataRepositoryImpl
import com.indie.apps.pennypal.repository.MerchantRepository
import com.indie.apps.pennypal.repository.MerchantRepositoryImpl
import com.indie.apps.pennypal.repository.NetworkRepository
import com.indie.apps.pennypal.repository.NetworkRepositoryImpl
import com.indie.apps.pennypal.repository.PaymentModeRepository
import com.indie.apps.pennypal.repository.PaymentModeRepositoryImpl
import com.indie.apps.pennypal.repository.PaymentRepository
import com.indie.apps.pennypal.repository.PaymentRepositoryImpl
import com.indie.apps.pennypal.repository.PreferenceRepository
import com.indie.apps.pennypal.repository.PreferenceRepositoryImpl
import com.indie.apps.pennypal.repository.UserRepository
import com.indie.apps.pennypal.repository.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://v6.exchangerate-api.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideExchangeRateApiService(retrofit: Retrofit): ExchangeRateApiService {
        return retrofit.create(ExchangeRateApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        countryRepository: CountryRepository
    ): AppDatabase = AppDatabase.getInstance(context, countryRepository)

    @Singleton
    @Provides
    fun provideCountryDatabase(@ApplicationContext context: Context): CountryDb =
        CountryDb.getInstance(context)

    @Singleton
    @Provides
    fun providePreference(@ApplicationContext context: Context): PreferenceManager =
        PreferenceManager.getInstance(context)

    @Provides
    @Singleton
    fun provideNetworkMonitor(@ApplicationContext context: Context): NetworkMonitor {
        return NetworkMonitor(context)
    }

    @Provides
    fun provideUserRepository(
        database: AppDatabase,
        countryRepository: CountryRepository,
        baseCurrencyRepository: BaseCurrencyRepository,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): UserRepository {
        return UserRepositoryImpl(
            database.userDao(),
            countryRepository,
            baseCurrencyRepository,
            dispatcher
        )
    }

    @Provides
    fun providePaymentRepository(
        database: AppDatabase,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): PaymentRepository {
        return PaymentRepositoryImpl(database.paymentDao(), dispatcher)
    }

    @Provides
    fun providePaymentModeRepository(
        database: AppDatabase,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): PaymentModeRepository {
        return PaymentModeRepositoryImpl(database.paymentModeDao(), dispatcher)
    }

    @Provides
    fun provideMerchantRepository(
        database: AppDatabase,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): MerchantRepository {
        return MerchantRepositoryImpl(database.merchantDao(), dispatcher)
    }

    @Provides
    fun provideMerchantDataRepository(
        database: AppDatabase,
        exchangeRateRepository: ExchangeRateRepository,
        userRepository: UserRepository,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): MerchantDataRepository {
        return MerchantDataRepositoryImpl(
            database.merchantDataDao(),
            exchangeRateRepository,
            userRepository,
            dispatcher
        )
    }

    @Provides
    fun provideCategoryRepository(
        database: AppDatabase,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): CategoryRepository {
        return CategoryRepositoryImpl(database.categoryDao(), dispatcher)
    }

    @Provides
    fun provideCountryRepository(countryDb: CountryDb): CountryRepository {
        return CountryRepositoryImpl(countryDb)
    }

    @Provides
    @Singleton
    fun provideContactsProvider(@ApplicationContext context: Context): ContactsProvider {
        return ContactsProviderImpl(
            contentResolver = context.contentResolver,
            dispatcher = Dispatchers.IO
        )
    }

    @Provides
    fun provideContactsRepo(contactsProvider: ContactsProvider): ContactsRepository {
        return ContactsRepositoryImpl(
            contactsProvider = contactsProvider
        )
    }

    @Provides
    fun providePreferenceRepo(preferenceManager: PreferenceManager): PreferenceRepository {
        return PreferenceRepositoryImpl(
            preferenceManager = preferenceManager
        )
    }

    @Provides
    fun provideBillingRepository(): BillingRepository {
        return BillingRepositoryImpl()
    }

    @Provides
    fun provideBudgetRepository(
        database: AppDatabase,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): BudgetRepository {
        return BudgetRepositoryImpl(
            database.budgetDao(),
            database.budgetCategoryDao(),
            database.merchantDataDao(),
            dispatcher
        )
    }

    @Provides
    fun provideAuthRepository(
        @ApplicationContext context: Context
    ): AuthRepository {
        return AuthRepositoryImpl(context)
    }

    @Provides
    fun provideBackupRepository(
        @ApplicationContext context: Context,
        authRepository: AuthRepository,
        countryRepository: CountryRepository,
        appDatabase: AppDatabase,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): BackupRepository {
        return BackupRepositoryImpl(
            context,
            authRepository,
            countryRepository,
            appDatabase,
            dispatcher
        )
    }


    @Provides
    fun provideBaseCurrencyRepository(
        database: AppDatabase,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): BaseCurrencyRepository {
        return BaseCurrencyRepositoryImpl(
            database.baseCurrencyDao(),
            dispatcher
        )
    }

    @Provides
    fun provideExchangeRateRepository(
        apiService: ExchangeRateApiService,
        countryRepository: CountryRepository,
        networkRepository: NetworkRepository,
        database: AppDatabase,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): ExchangeRateRepository {
        return ExchangeRateRepositoryImpl(
            apiService,
            database.exchangeRateDao(),
            countryRepository,
            networkRepository,
            dispatcher
        )
    }

    @Provides
    fun provideNetworkRepository(
        networkMonitor: NetworkMonitor,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): NetworkRepository {
        return NetworkRepositoryImpl(
            networkMonitor,
            dispatcher
        )
    }

}