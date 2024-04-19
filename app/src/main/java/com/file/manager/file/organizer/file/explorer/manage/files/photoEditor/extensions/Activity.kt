package com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.extensions

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import com.bumptech.glide.util.Util.isOnMainThread
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.firebase.FirebaseUtils.recordException


fun Activity.hideKeyboard() {
    try {
        if (isOnMainThread()) {
            hideKeyboardSync()
        } else {
            Handler(Looper.getMainLooper()).post {
                hideKeyboardSync()
            }
        }
    } catch (e: Exception) {
        e.recordException(javaClass.simpleName)
    }
}

fun Activity.hideKeyboardSync() {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow((currentFocus ?: View(this)).windowToken, 0)
    window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    currentFocus?.clearFocus()
}