package com.multithread.screencapture.history.data.repository

import com.multithread.screencapture.history.data.local.LocalImageDataSource
import com.multithread.screencapture.history.model.ImageDataModel
import javax.inject.Inject

class ImageRepository @Inject constructor(private val localImageDataSource: LocalImageDataSource) {


    suspend fun getHistory() = localImageDataSource.getHistory()
    suspend fun saveImage(image: ImageDataModel) = localImageDataSource.saveImage(image)
    suspend fun deleteHistoryById(id: Long) = localImageDataSource.deleteHistoryById(id)

}