package com.multithread.screencapture.utils

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.multithread.screencapture.history.adapter.HistoryAdapter
import com.multithread.screencapture.history.model.ImageDataModel


@BindingAdapter("app:items")
fun setSyncItems(listView: RecyclerView, items: List<ImageDataModel>?) {
    (listView.adapter as HistoryAdapter?)?.replaceData(items)
}
