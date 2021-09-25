package com.multithread.screencapture.home.viewmodel

import android.app.Application
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Picture
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.View
import android.webkit.WebView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.multithread.screencapture.history.data.repository.ImageRepository
import com.multithread.screencapture.history.model.ImageDataModel
import com.multithread.screencapture.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.*
import javax.inject.Inject
import android.provider.MediaStore
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.qualifiers.ApplicationContext


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val imageRepository: ImageRepository,
    @ApplicationContext val context: Context
) : ViewModel() {
    val isLoading = MutableLiveData<Boolean>(false)
    val isImageSaved = MutableLiveData<Boolean>(false)

    val imageBitmap = MutableLiveData<Bitmap>()

    val imageTitle = MutableLiveData<String>()
    val actualImagePath = MutableLiveData<String>()
    val dateTime = MutableLiveData<Long>()

    fun getImageFileName(): String {
        dateTime.value = Calendar.getInstance().timeInMillis
        return dateTime.value.toString()
    }

     fun saveImage() {
        if (actualImagePath.value != null && imageTitle.value != null && dateTime.value != null) {
            val imageObj = ImageDataModel(
                imagePath = actualImagePath.value!!,
                title = imageTitle.value!!,
                date = dateTime.value!!
            )
            viewModelScope.launch {
                imageRepository.saveImage(imageObj)
                isImageSaved.value = true
            }
        }

    }

}
