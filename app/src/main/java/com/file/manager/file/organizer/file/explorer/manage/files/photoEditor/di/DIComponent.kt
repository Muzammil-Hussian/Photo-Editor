package com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.di

import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.photoViewer.PhotoEditorViewModel
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.viewModel.PicturesViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class DIComponent : KoinComponent {

    val picturesViewModel by inject<PicturesViewModel>()

}