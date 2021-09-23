package com.multithread.screencapture.di

import com.multithread.screencapture.history.data.local.LocalImageDataSource
import com.multithread.screencapture.history.data.repository.ImageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideImageRepository(localImageDataSource: LocalImageDataSource) =
        ImageRepository(localImageDataSource)
}