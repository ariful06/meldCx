package com.multithread.screencapture.history

import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.multithread.screencapture.history.data.repository.ImageRepository
import com.multithread.screencapture.history.model.ImageDataModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val imageRepository: ImageRepository
) : ViewModel() {

    private val _imageHistoryLiveData = MutableLiveData<List<ImageDataModel>>()
    val imageHistoryLiveData: LiveData<List<ImageDataModel>>
        get() = _imageHistoryLiveData


    private var completeList: MutableList<ImageDataModel> = ArrayList<ImageDataModel>()
    var searchText = MutableLiveData<String>()
    var onDeleteEvent = MutableLiveData<Boolean>(false)
    var onItemClickEvent = MutableLiveData<ImageDataModel>()

    fun getAllImageList() {
        completeList.clear()
        viewModelScope.launch {
            val list = imageRepository.getHistory()
            _imageHistoryLiveData.value = (list ?: emptyList())
            completeList.addAll(_imageHistoryLiveData.value ?: emptyList())
        }
    }

    var searchWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) {
            searchText.value = s.toString()
            filterList(s)
        }
    }


    private fun filterList(s: CharSequence) {
        val key = s.toString().toLowerCase().trim { it <= ' ' }
        if (key.isEmpty()) {
            _imageHistoryLiveData.value = emptyList()
            _imageHistoryLiveData.value = completeList
            return
        }
        val filteredList: MutableList<ImageDataModel> = ArrayList<ImageDataModel>()
        if (!completeList.isNullOrEmpty()) {
            for (item in completeList) {
                try {
                    val title: String? = item.title
                    if (title != null && title.contains(key, true)) {
                        filteredList.add(item)
                    }
                } catch (e: Exception) {
                }
            }
        }
        _imageHistoryLiveData.value = emptyList()
        _imageHistoryLiveData.value = filteredList
    }

    fun deleteItem(imageObj: ImageDataModel?) {
        if (imageObj != null) {
            viewModelScope.launch {
                imageRepository.deleteHistoryById(imageObj.id!!)
                val file = File(imageObj.imagePath)
                if (file.exists()) {
                    file.delete()
                }
                onDeleteEvent.value = true
            }
        }
    }

}