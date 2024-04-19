package com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.componentView.recyclerView.listeners

interface MyGestureListener {
    fun getLastUp(): Long

    fun getScaleFactor(): Float

    fun setScaleFactor(value: Float)

    fun getZoomListener(): MyZoomListener?
}