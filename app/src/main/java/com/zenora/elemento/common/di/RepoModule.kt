package com.zenora.elemento.common.di

import com.zenora.elemento.common.network.BBDeployDemoAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {
    @Singleton
    @Provides
    fun provideWebService() = BBDeployDemoAPI()
}