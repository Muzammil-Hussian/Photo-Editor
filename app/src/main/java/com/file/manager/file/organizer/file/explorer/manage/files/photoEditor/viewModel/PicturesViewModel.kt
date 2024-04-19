package com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.model.DateGroupedItems
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.repository.PicturesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class PicturesViewModel(private val repository: PicturesRepository) : ViewModel() {

    private val _groupedPicturesByDateUiState = MutableLiveData<ArrayList<DateGroupedItems>>()
    val groupedPicturesByDateUiState get() = _groupedPicturesByDateUiState



     fun fetchPicturesListGroupedByDate() {
        viewModelScope.launch(Dispatchers.IO) { _groupedPicturesByDateUiState.postValue(repository.picturesListGroupedByDate()) }
    }
}