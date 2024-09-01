package com.indie.apps.pennypal.di

import android.content.Context
import com.indie.apps.contacts.data.provider.ContactsProvider
import com.indie.apps.contacts.data.provider.impl.ContactsProviderImpl
import com.indie.apps.contacts.data.repo.ContactsRepository
import com.indie.apps.contacts.data.repo.impl.ContactsRepositoryImpl
import com.indie.apps.cpp.data.CountryDb
import com.indie.apps.cpp.data.repository.CountryRepository
import com.indie.apps.cpp.data.repository.CountryRepositoryImpl
import com.indie.apps.pennypal.data.db.AppDatabase
import com.indie.apps.pennypal.repository.MerchantDataRepository
import com.indie.apps.pennypal.repository.MerchantDataRepositoryImpl
import com.indie.apps.pennypal.repository.MerchantRepository
import com.indie.apps.pennypal.repository.MerchantRepositoryImpl
import com.indie.apps.pennypal.repository.PaymentRepository
import com.indie.apps.pennypal.repository.PaymentRepositoryImpl
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
    fun provideAppDatabase(@ApplicationContext context : Context, countryRepository: CountryRepository) : AppDatabase = AppDatabase.getInstance(context, countryRepository)

    @Singleton
    @Provides
    fun provideCountryDatabase(@ApplicationContext context : Context) : CountryDb = CountryDb.getInstance(context)

    @Provides
    fun provideUserRepository(database: AppDatabase): UserRepository {
         return UserRepositoryImpl(database.userDao())
    }

    @Provides
    fun providePaymentRepository(database: AppDatabase): PaymentRepository {
        return PaymentRepositoryImpl(database.paymentDao())
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
}