package com.file.manager.file.organizer.file.explorer.manage.files.photoEditor

import android.app.Application
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AppClass : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@AppClass)
            modules(appModules)
        }
    }
}
