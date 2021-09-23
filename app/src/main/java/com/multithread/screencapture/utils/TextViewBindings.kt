package com.multithread.screencapture.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.multithread.screencapture.R
import com.multithread.screencapture.utils.DateUtils.VIEW_DATE_FORMAT
import java.util.*

object TextViewBindings {
    @JvmStatic
    @BindingAdapter("app:date")
    fun setDate(textView: TextView, date: Long?) {
        try {
            if (date == null)
                textView.text = textView.context.getText(R.string.hints_date_format)
            else
                textView.text = DateUtils.formatDate(date, VIEW_DATE_FORMAT)
        } catch (e: Exception) {
            textView.text = DateUtils.formatDate(
                Calendar.getInstance().timeInMillis,
                VIEW_DATE_FORMAT
            )
        }
    }

}


