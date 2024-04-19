package com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.photoViewer.background

sealed class BackgroundItemsModel {
    abstract fun getTypeIdentifier(): String

    data class SolidColor(val color: Int) : BackgroundItemsModel() {
        override fun getTypeIdentifier() = SOLID_COLOR
    }

    data class GradientColor(val colors: IntArray) : BackgroundItemsModel() {
        override fun getTypeIdentifier() = GRADIENT_COLOR
    }

    data class Pattern(val resourceId: Int) : BackgroundItemsModel() {
        override fun getTypeIdentifier() = PATTERN
    }
}

const val SOLID_COLOR = "SolidColor"
const val GRADIENT_COLOR = "GradientColor"
const val PATTERN = "Pattern"