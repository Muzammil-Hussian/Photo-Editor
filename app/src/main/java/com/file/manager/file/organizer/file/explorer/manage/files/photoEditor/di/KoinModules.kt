package com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.di

import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.photoViewer.PhotoEditorRepository
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.photoViewer.PhotoEditorRepositoryImpl
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.photoViewer.PhotoEditorViewModel
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.repository.PicturesRepository
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.repository.PicturesRepositoryImpl
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.viewModel.PicturesViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module


val reposModule = module {
    single { PicturesRepositoryImpl(get()) } bind PicturesRepository::class
    single { PhotoEditorRepositoryImpl(get()) } bind PhotoEditorRepository::class
    singleOf(::PhotoEditorRepositoryImpl) { bind<PhotoEditorRepository>() }

}

val viewModelModule = module {
    viewModel {
        PicturesViewModel(get())
    }

    viewModel {
        PhotoEditorViewModel(get())
    }
}


val appModules = listOf(reposModule, viewModelModule)