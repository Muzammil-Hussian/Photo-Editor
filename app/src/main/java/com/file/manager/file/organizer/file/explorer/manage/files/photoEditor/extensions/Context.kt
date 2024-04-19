package com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.extensions

import android.content.ContentValues.TAG
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.google.android.datatransport.BuildConfig


fun Context.queryCursor(
    uri: Uri,
    projection: Array<String>,
    selection: String? = null,
    selectionArgs: Array<String>? = null,
    sortOrder: String? = null,
    showErrors: Boolean = false,
    callback: (cursor: Cursor) -> Unit
) {
    try {
        val cursor = contentResolver.query(uri, projection, selection, selectionArgs, sortOrder)
        cursor?.use {
            if (cursor.moveToFirst()) {
                do {
                    callback(cursor)
                } while (cursor.moveToNext())
            }
        }
    } catch (e: Exception) {
        if (showErrors || BuildConfig.DEBUG) {
            Log.e(TAG, "queryCursor: ${e.printStackTrace()}")
            //   showErrorToast(e)
        }
    }
}
