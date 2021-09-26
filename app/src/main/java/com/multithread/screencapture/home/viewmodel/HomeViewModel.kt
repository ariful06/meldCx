package com.multithread.screencapture.home.viewmodel

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.multithread.screencapture.history.data.repository.ImageRepository
import com.multithread.screencapture.history.model.ImageDataModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


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
