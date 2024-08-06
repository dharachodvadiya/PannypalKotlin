package com.indie.apps.pannypal.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@TestInstallIn(components = [SingletonComponent::class], replaces = [DispatcherModule::class])
@Module
class DispatcherModuleTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @DefaultDispatcher
    @Provides
    fun providesDefaultDispatcher(): CoroutineDispatcher = testDispatcher

    @IoDispatcher
    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = testDispatcher

    @MainDispatcher
    @Provides
    fun providesMainDispatcher(): CoroutineDispatcher = testDispatcher
}