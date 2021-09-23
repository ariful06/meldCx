package com.multithread.screencapture.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter

object ImageViewBindings {

    @JvmStatic
    @BindingAdapter("app:setImage")
    fun setImage(imageView: ImageView, imagePath: String?) {
        try {
            if (imagePath == null)
                return
            else {
                val bmImg = BitmapFactory.decodeFile(imagePath)
                val resized = Bitmap.createScaledBitmap(bmImg!!, 180, 180, true)
                imageView.setImageBitmap(resized)
            }

        } catch (e: Exception) {
            Log.e("ImageViewBindings", "Error :${e.message}")
        }
    }

}