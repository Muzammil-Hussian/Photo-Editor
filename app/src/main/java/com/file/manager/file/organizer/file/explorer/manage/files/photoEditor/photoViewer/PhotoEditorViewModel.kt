package com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.photoViewer

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

private const val TAG = "PhotoEditorViewModel"

class PhotoEditorViewModel(private val repository: PhotoEditorRepository) : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _resultUri = MutableLiveData<Uri>()
    val resultUri get() = _resultUri

    fun showLoading() {
        _isLoading.value = true
    }

    fun hideLoading() {
        _isLoading.value = false
    }

    fun bitmapToUri(bitmap: Bitmap): Uri? = repository.convertBitmapToUri(bitmap)

    fun setUri(uri: Uri) {
        Log.i(TAG, "setUri: $uri")
        _resultUri.value = uri
    }

    fun fetchUriFromMediaStorePath(imagePath: String): Job {
        return viewModelScope.launch { _resultUri.value = repository.getImageContentUri(imagePath) }
    }
}