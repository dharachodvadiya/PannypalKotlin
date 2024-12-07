package com.indie.apps.pennypal.di

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.indie.apps.contacts.data.provider.ContactsProvider
import com.indie.apps.contacts.data.provider.impl.ContactsProviderImpl
import com.indie.apps.contacts.data.repo.ContactsRepository
import com.indie.apps.contacts.data.repo.impl.ContactsRepositoryImpl
import com.indie.apps.cpp.data.CountryDb
import com.indie.apps.cpp.data.repository.CountryRepository
import com.indie.apps.cpp.data.repository.CountryRepositoryImpl
import com.indie.apps.pennypal.data.database.db.AppDatabase
import com.indie.apps.pennypal.data.preference.PreferenceManager
import com.indie.apps.pennypal.repository.AuthRepository
import com.indie.apps.pennypal.repository.AuthRepositoryImpl
import com.indie.apps.pennypal.repository.BackupRepository
import com.indie.apps.pennypal.repository.BackupRepositoryImpl
import com.indie.apps.pennypal.repository.BillingRepository
import com.indie.apps.pennypal.repository.BillingRepositoryImpl
import com.indie.apps.pennypal.repository.BudgetRepository
import com.indie.apps.pennypal.repository.BudgetRepositoryImpl
import com.indie.apps.pennypal.repository.CategoryRepository
import com.indie.apps.pennypal.repository.CategoryRepositoryImpl
import com.indie.apps.pennypal.repository.MerchantDataRepository
import com.indie.apps.pennypal.repository.MerchantDataRepositoryImpl
import com.indie.apps.pennypal.repository.MerchantRepository
import com.indie.apps.pennypal.repository.MerchantRepositoryImpl
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
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

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
    fun provideUserRepository(database: AppDatabase): UserRepository {
        return UserRepositoryImpl(database.userDao())
    }

    @Provides
    fun providePaymentRepository(database: AppDatabase): PaymentRepository {
        return PaymentRepositoryImpl(database.paymentDao())
    }

    @Provides
    fun providePaymentModeRepository(database: AppDatabase): PaymentModeRepository {
        return PaymentModeRepositoryImpl(database.paymentModeDao())
    }

    @Provides
    fun provideMerchantRepository(database: AppDatabase): MerchantRepository {
        return MerchantRepositoryImpl(database.merchantDao())
    }

    @Provides
    fun provideMerchantDataRepository(database: AppDatabase): MerchantDataRepository {
        return MerchantDataRepositoryImpl(database.merchantDataDao())
    }

    @Provides
    fun provideCategoryRepository(database: AppDatabase): CategoryRepository {
        return CategoryRepositoryImpl(database.categoryDao())
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
        database: AppDatabase
    ): BudgetRepository {
        return BudgetRepositoryImpl(
            database.budgetDao(),
            database.budgetCategoryDao(),
            database.merchantDataDao()
        )
    }

    @Provides
    fun provideAuthRepository(
        googleSignInClient: GoogleSignInClient,
        @ApplicationContext context: Context
    ): AuthRepository {
        return AuthRepositoryImpl(googleSignInClient, context)
    }

    @Provides
    fun provideBackupRepository(
        @ApplicationContext context: Context,
        authRepository: AuthRepository
    ): BackupRepository {
        return BackupRepositoryImpl(context, authRepository)
    }

    @Provides
    fun getGoogleSignInClient(@ApplicationContext context: Context): GoogleSignInClient {
        val signInOptions = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("543082646910-is4lcvu1m56v6u2pjaeq1mqf2ndutved.apps.googleusercontent.com")
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(context, signInOptions)
    }
}