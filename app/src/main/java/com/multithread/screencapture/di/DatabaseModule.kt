package com.multithread.screencapture.di

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.multithread.screencapture.db.AppLocalDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) =
        AppLocalDatabase.getLocalDatabase(appContext)


    @Singleton
    @Provides
    fun provideDbTransactionDao(db: AppLocalDatabase) = db.getImageDao()

}