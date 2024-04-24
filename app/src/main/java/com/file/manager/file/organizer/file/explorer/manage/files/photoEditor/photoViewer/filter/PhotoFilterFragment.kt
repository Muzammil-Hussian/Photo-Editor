package com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.photoViewer.filter

import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.R
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.databinding.FragmentPhotoFilterBinding
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.extensions.beGone
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.extensions.beVisible
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.photoViewer.PhotoViewerUtil
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.photoViewer.dialog.showUnsavedChangesDialog
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.ui.base.AbsLoadingDialog
import ja.burhanrashid52.photoeditor.OnPhotoEditorListener
import ja.burhanrashid52.photoeditor.OnSaveBitmap
import ja.burhanrashid52.photoeditor.PhotoEditor
import ja.burhanrashid52.photoeditor.ViewType

private const val TAG = "PhotoFilterFragmentLogs"

class PhotoFilterFragment : AbsLoadingDialog<FragmentPhotoFilterBinding>() {

    private lateinit var adapter: FilterViewAdapter

    private val photoEditor by lazy { PhotoEditor.Builder(requireContext(), binding.image).build() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeData()
        setupPhotoViewer()
        setupAdapter()
        clickListener()
    }

    private fun observeData() {
        photoEditorViewModel.resultUri.observe(viewLifecycleOwner) {
            Log.i(TAG, "observeData: resultUri: $it")
            Glide.with(requireActivity()).load(it).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(binding.image.source)

            Glide.with(globalContext).load(it).skipMemoryCache(true).into(binding.previewImage)
//            binding.photoEditorView.source.setImageURI(it)
        }
    }

    private fun clickListener() {
        binding.apply {
            close.setOnClickListener { mainActivity.showUnsavedChangesDialog { popFrom(R.id.photoFilterFragment) } }
            preview.setOnTouchListener { _, motionEvent ->
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        binding.previewImage.beVisible()
                    }

                    MotionEvent.ACTION_UP -> {
                        binding.previewImage.beGone()
                    }
                }
                return@setOnTouchListener true
            }
            done.setOnClickListener {
                photoEditorViewModel.showLoading()
                photoEditor.saveAsBitmap(object : OnSaveBitmap {
                    override fun onBitmapReady(saveBitmap: Bitmap) {

                        photoEditorViewModel.bitmapToUri(saveBitmap)?.let {
                            Log.i(TAG, "onBitmapReady: uri: $it")
                            photoEditorViewModel.setUri(it)

                           /* MediaScannerConnection.scanFile(globalContext, arrayOf(it.toString()), null) { path, p1 ->
                                Log.i(TAG, "onBitmapReady: $path")
                            }*/
                        } ?: {
                            Log.e(TAG, "Error while creating uri")
                        }

                        photoEditorViewModel.hideLoading()
                        popFrom(R.id.photoFilterFragment)
                    }
                })
            }
        }
    }

    private fun setupAdapter() {
        adapter = FilterViewAdapter { photoEditor.setFilterEffect(it) }
        binding.recyclerView.adapter = adapter
        adapter.submitList(PhotoViewerUtil.filtersPairList)
    }

    private fun setupPhotoViewer() {
        photoEditor.setOnPhotoEditorListener(object : OnPhotoEditorListener {
            override fun onAddViewListener(viewType: ViewType, numberOfAddedViews: Int) {
                Log.i(TAG, "onAddViewListener: ")
            }

            override fun onEditTextChangeListener(rootView: View, text: String, colorCode: Int) {
                Log.i(TAG, "onEditTextChangeListener: ")
            }

            override fun onRemoveViewListener(viewType: ViewType, numberOfAddedViews: Int) {
                Log.i(TAG, "onRemoveViewListener: ")
            }

            override fun onStartViewChangeListener(viewType: ViewType) {
                Log.i(TAG, "onStartViewChangeListener: ")
            }

            override fun onStopViewChangeListener(viewType: ViewType) {
                Log.i(TAG, "onStopViewChangeListener: ")
            }

            override fun onTouchSourceImage(event: MotionEvent) {
                Log.i(TAG, "onTouchSourceImage: ")
            }
        })
    }
}