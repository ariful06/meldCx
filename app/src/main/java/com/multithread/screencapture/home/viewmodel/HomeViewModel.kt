package com.multithread.screencapture.home.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Picture
import android.os.Environment
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.multithread.screencapture.history.data.repository.ImageRepository
import com.multithread.screencapture.history.model.ImageDataModel
import com.multithread.screencapture.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.coroutineContext
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class HomeViewModel @Inject constructor(
    val imageRepository: ImageRepository
) : ViewModel() {

    val isLoading = MutableLiveData<Boolean>(false)
    val isImageSaved = MutableLiveData<Boolean>(false)

    fun getSnapshot(picture: Picture, url: String) {
        val width = picture.width
        val height = picture.height
        if (width > 0 && height > 0) {
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            picture.draw(canvas)
            var outputStream: FileOutputStream? = null
            val file = Environment.getExternalStorageDirectory()
            val dir = File(file.absolutePath + "${Constants.FOLDER_NAME}")
            dir.mkdirs()
            val date = Calendar.getInstance().timeInMillis
            val filename = "${date}.jpg"
            val outfile = File(dir, filename)
            try {
                outputStream = FileOutputStream(outfile)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)

                outputStream.flush()
                outputStream.close()
            } catch (e: Exception) {
                Log.d("SavingError", "SaveImageToGallery: " + e.message)
            }

            saveImage(ImageDataModel(imagePath = outfile.toString(), title = url, date = date))
        }
    }

    private fun saveImage(image: ImageDataModel) {
        viewModelScope.launch{
            imageRepository.saveImage(image)
            isImageSaved.value = true
        }
    }
}
