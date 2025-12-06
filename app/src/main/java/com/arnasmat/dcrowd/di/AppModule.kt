package com.arnasmat.dcrowd.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Application-level Hilt module for providing dependencies with application-wide scope.
 *
 * Add @Provides methods here to provide application-wide dependencies.
 * Example:
 * @Provides
 * @Singleton
 * fun provideYourDependency(): YourDependency = YourDependency()
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule

