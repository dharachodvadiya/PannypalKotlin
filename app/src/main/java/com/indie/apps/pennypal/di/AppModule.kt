package com.indie.apps.pennypal.di

import android.content.Context
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
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context : Context) : AppDatabase = AppDatabase.getInstance(context)

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
}