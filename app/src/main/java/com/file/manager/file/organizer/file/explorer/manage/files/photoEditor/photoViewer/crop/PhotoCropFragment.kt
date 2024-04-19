package com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.photoViewer.crop

import android.os.Bundle
import android.util.Log
import android.view.View
import com.canhub.cropper.CropImageView
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.R
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.databinding.FragmentPhotoCropBinding
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.photoViewer.PhotoViewerUtil
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.photoViewer.dialog.showUnsavedChangesDialog
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.ui.base.AbsLoadingDialog

private const val TAG = "PhotoCropFragmentLogs"

class PhotoCropFragment : AbsLoadingDialog<FragmentPhotoCropBinding>(), CropImageView.OnCropImageCompleteListener {

    private lateinit var adapter: CropAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialization()
        observeData()
        setupAdapter()
        clickListeners()
    }

    private fun initialization() {
        binding.cropImageView.apply {
            maxZoom = 8
            isShowProgressBar = false
            setOnCropImageCompleteListener(this@PhotoCropFragment)
        }
    }

    private fun observeData() {
        photoEditorViewModel.resultUri.observe(viewLifecycleOwner) {
            Log.i(TAG, "resultUri: $it")
            binding.cropImageView.setImageUriAsync(it)
        }
    }

    override fun onCropImageComplete(view: CropImageView, result: CropImageView.CropResult) {
        if (result.error == null) {
            Log.i(TAG, "onCropImageComplete: Original bitmap: ${result.originalBitmap}")
            Log.i(TAG, "onCropImageComplete: Original uri: ${result.originalUri}")
            Log.i(TAG, "onCropImageComplete: Output bitmap: ${result.bitmap}")
            Log.i(TAG, "onCropImageComplete: Output uri: ${result.getUriFilePath(view.context)}")
            photoEditorViewModel.bitmapToUri(result.bitmap!!)?.let {
                Log.i(TAG, "onBitmapReady: uri: $it")
                photoEditorViewModel.setUri(it)
            } ?: {
                photoEditorViewModel.setUri(result.uriContent!!)
                Log.e(TAG, "Error while creating uri")
            }
//            result.uriContent?.let { photoEditorViewModel.setUri(it) }
        } else {
            Log.e(TAG, "${result.error} Failed to crop image")
            showToast("Crop failed: ${result.error?.message}")
        }
        photoEditorViewModel.hideLoading()
        popFrom(R.id.photoCropFragment)
    }


    private fun setupAdapter() {
        adapter = CropAdapter { item ->
            binding.cropImageView.apply {
                when (item.name) {
                    R.string.ratio_1_1 -> setAspectRatio(1, 1)
                    R.string.ratio_4_3 -> setAspectRatio(4, 3)
                    R.string.ratio_16_9 -> setAspectRatio(16, 9)
                    R.string.ratio_3_2 -> setAspectRatio(3, 2)
                    R.string.ratio_9_16 -> setAspectRatio(9, 16)
                    R.string.ratio_2_3 -> setAspectRatio(2, 3)
                    R.string.ratio_5_4 -> setAspectRatio(5, 4)
                    R.string.ratio_4_5 -> setAspectRatio(4, 5)
                    R.string.ratio_3_1 -> setAspectRatio(3, 1)
                    R.string.ratio_3_4 -> setAspectRatio(3, 4)
                    else -> setFixedAspectRatio(false)
                }
            }
        }
        binding.include.recyclerView.adapter = adapter
        adapter.submitList(PhotoViewerUtil.cropRatioList)
    }

    private fun clickListeners() {
        binding.include.apply {
            close.setOnClickListener { mainActivity.showUnsavedChangesDialog(leaveCallback = { popFrom(R.id.photoCropFragment) }) }

            done.setOnClickListener {
                photoEditorViewModel.showLoading()
                binding.cropImageView.croppedImageAsync()
            }
        }
    }

    override fun onDestroyView() {
        binding.cropImageView.setOnCropImageCompleteListener(null)
        super.onDestroyView()
    }
}
