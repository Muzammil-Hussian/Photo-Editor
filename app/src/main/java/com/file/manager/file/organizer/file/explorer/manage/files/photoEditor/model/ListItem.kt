package com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.model

data class ListItem(
    var path: String,
    var name: String = "",
    var isDirectory: Boolean = false,
    var children: Int = 0,
    var size: Long = 0L,
    var modified: Long = 0L,
    var isFavorite: Boolean = false,
    var isChecked: Boolean = false

)