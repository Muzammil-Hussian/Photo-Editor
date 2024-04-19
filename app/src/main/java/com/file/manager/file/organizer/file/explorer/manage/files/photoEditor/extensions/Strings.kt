package com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.extensions

fun String.getFilenameFromPath() = substring(lastIndexOf("/") + 1)

val videoExtensions: Array<String> get() = arrayOf(".mp4", ".mkv", ".webm", ".avi", ".3gp", ".mov", ".m4v", ".3gpp")

fun String.isVideoFast() = videoExtensions.any { endsWith(it, true) }
