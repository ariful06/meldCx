package com.multithread.screencapture.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    const val VIEW_DATE_FORMAT = "dd-MM-yyyy hh:mm a"
    fun formatDate(timeInMillis: Long, outputFormat: String? = null): String {
        return SimpleDateFormat(outputFormat ?: VIEW_DATE_FORMAT, Locale.ENGLISH).format(
            timeInMillis
        )
    }
}