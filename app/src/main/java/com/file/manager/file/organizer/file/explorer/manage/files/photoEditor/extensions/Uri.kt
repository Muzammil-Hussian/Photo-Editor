package com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri

/**
 * Extension function to convert Uri to Bitmap.
 *
 * @param context The context used to access the ContentResolver.
 * @return Bitmap? A Bitmap object or null if the conversion fails.
 */
fun Uri.toBitmap(context: Context): Bitmap? {
    return try {
        context.contentResolver.openInputStream(this)?.use { inputStream ->
            BitmapFactory.decodeStream(inputStream)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
