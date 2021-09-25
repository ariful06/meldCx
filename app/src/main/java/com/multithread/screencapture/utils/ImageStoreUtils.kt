package com.multithread.screencapture.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

object ImageStoreUtils {

    @Throws(IOException::class)
    open fun getRealPath(context: Context, bitmap: Bitmap?, fileName: String): String {
        if (bitmap == null) return ""
        var fos: OutputStream? = null
        var imageUri: Uri? = null
        try {

            val resolver = context.contentResolver
            val contentValues = ContentValues()
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + Constants.FOLDER_NAME_WITHOUT_SEPARATOR)
            imageUri =
                resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            if (imageUri == null) throw IOException("Failed to create new MediaStore record.")
            fos = resolver.openOutputStream(imageUri)

            if (!bitmap.compress(
                    Bitmap.CompressFormat.PNG,
                    60,
                    fos
                )
            ) throw IOException("Failed to save bitmap.")
            fos!!.flush()
        } finally {
            fos?.close()
        }
        return  PathUtils.getPath(context, imageUri)?:""
    }



    fun getRealPath(bitmap: Bitmap?,fileName: String) : String {
        if (bitmap == null) return ""
            var outputStream: FileOutputStream? = null
            val file = Environment.getExternalStorageDirectory()
            val dir = File(file.absolutePath + "${Constants.FOLDER_NAME}")
            dir.mkdirs()

            val filename = "${fileName}.jpg"
            val outfile = File(dir, filename)
            try {
                outputStream = FileOutputStream(outfile)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
                outputStream.flush()
                outputStream.close()
            } catch (e: Exception) {
                Log.d("SavingError", "SaveImageToGallery: " + e.message)
                return ""
            }
        return outfile.toString()
    }


    fun getBitmapFromView(view : View) : Bitmap? {
        val width = view.width
        val height = view.height
        var bitmap: Bitmap? = null
        val canvas: Canvas?
        if (width > 0 && height > 0) {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            if(bitmap != null){
                canvas = Canvas(bitmap)
                view.draw(canvas)
            }
        }
       return bitmap
    }

}