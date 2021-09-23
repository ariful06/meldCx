package com.multithread.screencapture.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.multithread.screencapture.history.model.ImageDataModel

@Dao
interface ImageDao {
    @Insert
    suspend fun insert(image: ImageDataModel?)

    @Query("SELECT * FROM table_history")
    suspend fun getHistory(): List<ImageDataModel>?

    @Query("DELETE from table_history where id =:id")
    suspend fun deleteItem(id: Long)
}