package com.multithread.screencapture.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.multithread.screencapture.dao.ImageDao
import com.multithread.screencapture.history.model.ImageDataModel

@Database(
    entities = [ImageDataModel::class],
    version = 1,
    exportSchema = false
)
abstract class AppLocalDatabase : RoomDatabase() {
    abstract fun getImageDao(): ImageDao

    companion object {

        @Volatile
        private var instance: AppLocalDatabase? = null

        fun getLocalDatabase(context: Context): AppLocalDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(
                appContext,
                AppLocalDatabase::class.java,
                "screen_shot_db"
            )
                .build()
    }

}