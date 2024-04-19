package com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.photoViewer.editor

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.R
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.databinding.FragmentPhotoEditorBinding
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.extensions.beGone
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.extensions.beVisible
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.photoViewer.PhotoViewerUtil
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.ui.base.AbsLoadingDialog
import com.google.android.material.textview.MaterialTextView
import ja.burhanrashid52.photoeditor.PhotoEditor
import ja.burhanrashid52.photoeditor.SaveFileResult
import ja.burhanrashid52.photoeditor.SaveSettings
import kotlinx.coroutines.launch


private const val TAG = "PhotoEditorFragment"

class PhotoEditorFragment : AbsLoadingDialog<FragmentPhotoEditorBinding>() {

    private lateinit var adapter: PhotoEditorAdapter
    private val mSaveFileHelper by lazy { FileSaveHelper(mainActivity) }
    private val photoEditor by lazy { PhotoEditor.Builder(globalContext, binding.image).build() }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()

        photoEditorViewModel.resultUri.observe(viewLifecycleOwner) {
            Log.i(TAG, "resultUri: $it")
//            Glide.with(globalContext).load(it).into(binding.image.source)
            binding.image.source.setImageURI(it)

        }

        activity?.findViewById<MaterialTextView>(R.id.save)?.apply {
            beVisible()
            setOnClickListener {
                saveImage()
            }
        }
    }


    @RequiresPermission(allOf = [Manifest.permission.WRITE_EXTERNAL_STORAGE])
    private fun saveImage() {
        photoEditorViewModel.showLoading()
        val fileName = System.currentTimeMillis().toString() + ".png"
        val hasStoragePermission = ContextCompat.checkSelfPermission(globalContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        if (hasStoragePermission || FileSaveHelper.isSdkHigherThan28()) {
            mSaveFileHelper.createFile(fileName, object : FileSaveHelper.OnFileCreateResult {

                @RequiresPermission(allOf = [Manifest.permission.WRITE_EXTERNAL_STORAGE])
                override fun onFileCreateResult(
                    created: Boolean,
                    filePath: String?,
                    error: String?,
                    uri: Uri?
                ) {
                    lifecycleScope.launch {
                        if (created && filePath != null) {
                            val saveSettings = SaveSettings.Builder()
                                .setClearViewsEnabled(true)
                                .setTransparencyEnabled(true)
                                .build()

                            val result = photoEditor.saveAsFile(filePath, saveSettings)

                            if (result is SaveFileResult.Success) {
                                mSaveFileHelper.notifyThatFileIsNowPubliclyAvailable(globalContext.contentResolver)
                                photoEditorViewModel.hideLoading()
                                showSnackBar("Image Saved Successfully")
                                photoEditorViewModel.setUri(uri!!)
                                navigateToNextFragment(R.id.action_photoEditorFragment_to_shareImageFragment)
                            } else {
                                photoEditorViewModel.hideLoading()
                                showSnackBar("Failed to save Image")
                            }
                        } else {
                            photoEditorViewModel.hideLoading()
                            error?.let { showSnackBar(error) }
                        }
                    }
                }
            })
        } else {
            showToast("need permission")
            //requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    private fun setupAdapter() {
        adapter = PhotoEditorAdapter { position ->
            when (position) {
                0 -> navigateToNextFragment(R.id.action_photoEditorFragment_to_photoCropFragment)
                1 -> navigateToNextFragment(R.id.action_photoEditorFragment_to_photoRotateFragment)
                2 -> navigateToNextFragment(R.id.action_photoEditorFragment_to_photoAdjustFragment)
                3 -> navigateToNextFragment(R.id.action_photoEditorFragment_to_photoFilterFragment)
                4 -> navigateToNextFragment(R.id.action_photoEditorFragment_to_addTextFragment)
                5 -> navigateToNextFragment(R.id.action_photoEditorFragment_to_drawFragment)
                6 -> navigateToNextFragment(R.id.action_photoEditorFragment_to_backgroundFragment)
            }
        }
        binding.recyclerView.adapter = adapter
        adapter.submitList(PhotoViewerUtil.photoEditorList)
    }

    private fun navigateToNextFragment(id: Int) {
        navigateTo(R.id.photoEditorFragment, id)
    }

    override fun onDestroyView() {
        activity?.findViewById<MaterialTextView>(R.id.save)?.beGone()
        super.onDestroyView()
    }
}