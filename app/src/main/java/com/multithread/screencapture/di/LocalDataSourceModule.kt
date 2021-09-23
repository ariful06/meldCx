package com.multithread.screencapture.di

import com.multithread.screencapture.dao.ImageDao
import com.multithread.screencapture.history.data.local.LocalImageDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object LocalDataSourceModule {
    @Singleton
    @Provides
    fun provideHistoryLocalDataSource(imageDao: ImageDao) =
        LocalImageDataSource(imageDao)

}