package com.multithread.screencapture.history.data.local

import com.multithread.screencapture.dao.ImageDao
import com.multithread.screencapture.history.model.ImageDataModel
import javax.inject.Inject

class LocalImageDataSource @Inject constructor(
    private val imageDao: ImageDao
) {


    suspend fun getHistory() = imageDao.getHistory()
    suspend fun deleteHistoryById(id: Long) = imageDao.deleteItem(id)
    suspend fun saveImage(image: ImageDataModel) = imageDao.insert(image)
}