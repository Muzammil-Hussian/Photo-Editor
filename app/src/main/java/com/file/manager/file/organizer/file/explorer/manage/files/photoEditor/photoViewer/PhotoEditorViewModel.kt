package com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.photoViewer

import android.graphics.Bitmap
import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File

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

    fun fetchUriFromPath(path: String) = viewModelScope.launch { _resultUri.value = Uri.fromFile(File(path)) }

    fun bitmapToUri(bitmap: Bitmap): Uri? = repository.convertBitmapToUri(bitmap)
    fun bitmapToUri(bitmap: Bitmap, callback: (Uri?) -> Unit) = viewModelScope.launch(Dispatchers.IO) { callback.invoke(repository.convertBitmapToUri(bitmap)) }

    fun setUri(uri: Uri) {
//        Log.i(TAG, "setUri: $uri")
        _resultUri.value = "".toUri()
        _resultUri.value = uri
    }


    fun fetchUriFromMediaStorePath(imagePath: String): Job {
        return viewModelScope.launch { _resultUri.value = repository.getImageContentUri(imagePath) }
    }
}