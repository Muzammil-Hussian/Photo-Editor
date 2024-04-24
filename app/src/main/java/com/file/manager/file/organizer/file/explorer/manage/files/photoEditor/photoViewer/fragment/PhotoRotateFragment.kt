package com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.photoViewer.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.canhub.cropper.CropImageView
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.R
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.databinding.FragmentPhotoRotateBinding
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.extensions.beGone
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.photoViewer.PhotoViewerUtil
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.photoViewer.dialog.showUnsavedChangesDialog
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.photoViewer.editor.PhotoEditorAdapter
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.ui.base.AbsLoadingDialog

private const val TAG = "PhotoRotateFragmentLogs"

class PhotoRotateFragment : AbsLoadingDialog<FragmentPhotoRotateBinding>(), CropImageView.OnCropImageCompleteListener {

    private lateinit var adapter: PhotoEditorAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialization()
        observeData()
        setupAdapter()
        clickListeners()
    }

    private fun initialization() {
        binding.cropImageView.apply {
            isShowCropOverlay = false
            guidelines = CropImageView.Guidelines.OFF
            maxZoom = 8
            isShowProgressBar = false
            setOnCropImageCompleteListener(this@PhotoRotateFragment)
        }
    }

    private fun observeData() {
        photoEditorViewModel.resultUri.observe(viewLifecycleOwner) {
            Log.i(TAG, "observeData: uri $it")
            binding.cropImageView.setImageUriAsync(it)
        }
    }


    private fun setupAdapter() {
        adapter = PhotoEditorAdapter { position ->
            binding.cropImageView.apply {
                when (position) {
                    0 -> rotateImage(ROTATE_LEFT)
                    1 -> rotateImage(ROTATE_RIGHT)
                    2 -> flipImageHorizontally()
                    3 -> flipImageVertically()
                }
            }
        }
        binding.include.recyclerView.adapter = adapter
        binding.include.recyclerView.layoutManager = GridLayoutManager(globalContext, 4)
        adapter.submitList(PhotoViewerUtil.rotatePhotoList)
    }

    private fun clickListeners() {
        binding.include.apply {
            group.beGone()
            close.setOnClickListener { mainActivity.showUnsavedChangesDialog { popFrom(R.id.photoRotateFragment) } }
            done.setOnClickListener {
                photoEditorViewModel.showLoading()
                binding.cropImageView.croppedImageAsync()
            }
        }
    }

    override fun onCropImageComplete(view: CropImageView, result: CropImageView.CropResult) {
        if (result.error == null) {
            Log.i(TAG, "onCropImageComplete: Original bitmap: ${result.originalBitmap}")
            Log.i(TAG, "onCropImageComplete: Original uri: ${result.originalUri}")
            Log.i(TAG, "onCropImageComplete: Output bitmap: ${result.bitmap}")
            Log.i(TAG, "onCropImageComplete: Output uri: ${result.getUriFilePath(view.context)}")
            Log.i(TAG, "onCropImageComplete: uriContent: ${result.uriContent}")
            result.uriContent?.let { photoEditorViewModel.setUri(it) }

        } else {
            Log.e(TAG, "${result.error} Failed to crop image")
            showToast("Crop failed: ${result.error?.message}")
        }
        photoEditorViewModel.hideLoading()
        popFrom(R.id.photoRotateFragment)
    }

    override fun onDestroyView() {
        binding.cropImageView.setOnCropImageCompleteListener(null)
        super.onDestroyView()
    }

    companion object {
        private const val ROTATE_LEFT = -90
        private const val ROTATE_RIGHT = 90
    }
}


