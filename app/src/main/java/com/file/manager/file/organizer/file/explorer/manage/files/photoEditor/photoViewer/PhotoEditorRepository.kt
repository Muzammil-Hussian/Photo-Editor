package com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.photoViewer

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.extensions.getLongValue
import java.io.File
import java.io.FileOutputStream


interface PhotoEditorRepository {
    suspend fun getImageContentUri(imagePath: String): Uri?
    fun convertBitmapToUri(bitmap: Bitmap): Uri?
}


class PhotoEditorRepositoryImpl(private val context: Context) : PhotoEditorRepository {

    override suspend fun getImageContentUri(imagePath: String): Uri? {
        val cursor = context.contentResolver.query(
            /* uri = */ MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            /* projection = */ arrayOf(MediaStore.Images.Media._ID),
            /* selection = */ MediaStore.Images.Media.DATA + "=? ",
            /* selectionArgs = */ arrayOf(imagePath),
            /* sortOrder = */ null
        )

        return if (cursor != null && cursor.moveToFirst()) {
            val id = cursor.getLongValue(MediaStore.MediaColumns._ID)
            cursor.close()
            ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
        } else {
            if (File(imagePath).exists()) {
                val values = ContentValues()
                values.put(MediaStore.Images.Media.DATA, imagePath)
                context.contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
                )
            } else {
                null
            }
        }
    }

    override fun convertBitmapToUri(bitmap: Bitmap): Uri? {
        val file = File(context.cacheDir, "SampleImage.jpg")
        FileOutputStream(file).use { out -> bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out) }

        return try {
            FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}