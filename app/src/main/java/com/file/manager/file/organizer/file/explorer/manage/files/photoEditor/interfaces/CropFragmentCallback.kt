package com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.interfaces

import com.yalantis.ucrop.UCropFragment

interface CropFragmentCallback {

    /**
     * Return loader status
     * @param showLoader
     */
    fun loadingProgress(showLoader: Boolean)

    /**
     * Return cropping result or error
     * @param result
     */
    fun onCropFinish(result: UCropFragment.UCropResult?)
}