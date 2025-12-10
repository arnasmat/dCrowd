package com.arnasmat.dcrowd.di

import android.content.Context
import com.arnasmat.dcrowd.data.web3.CrowdSourcingWrapper
import com.arnasmat.dcrowd.data.web3.UserManager
import com.arnasmat.dcrowd.data.web3.Web3ConfigManager
import com.arnasmat.dcrowd.data.web3.Web3Factory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Application-level Hilt module for providing Web3 and blockchain dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideWeb3ConfigManager(@ApplicationContext context: Context): Web3ConfigManager {
        return Web3ConfigManager(context)
    }

    @Provides
    @Singleton
    fun provideWeb3Factory(web3ConfigManager: Web3ConfigManager): Web3Factory {
        return Web3Factory(web3ConfigManager)
    }

    @Provides
    @Singleton
    fun provideUserManager(@ApplicationContext context: Context): UserManager {
        return UserManager(context)
    }

    @Provides
    @Singleton
    fun provideCrowdSourcingWrapper(
        web3Factory: Web3Factory,
        userManager: UserManager,
        web3ConfigManager: Web3ConfigManager
    ): CrowdSourcingWrapper {
        return CrowdSourcingWrapper(web3Factory, userManager, web3ConfigManager)
    }
}

