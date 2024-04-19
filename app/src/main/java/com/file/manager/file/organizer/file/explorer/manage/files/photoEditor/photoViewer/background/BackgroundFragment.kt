package com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.photoViewer.background

import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.R
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.databinding.FragmentBackgroundBinding
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.extensions.beGone
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.extensions.beVisible
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.extensions.setPaddingDp
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.photoViewer.PhotoViewerUtil
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.photoViewer.dialog.showUnsavedChangesDialog
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.ui.base.BaseFragment
import com.google.android.material.tabs.TabLayout
import ja.burhanrashid52.photoeditor.OnSaveBitmap
import ja.burhanrashid52.photoeditor.PhotoEditor

private const val TAG = "BackgroundFragmentLogs"

class BackgroundFragment : BaseFragment<FragmentBackgroundBinding>() {


    private var showDiscardDialog = false

    private lateinit var adapter: BackgroundAdapter

    private val photoEditor by lazy { PhotoEditor.Builder(requireContext(), binding.photoEditorView).build() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        tabSelectedListener()
        onClickListeners()
        observeData()
    }

    private fun observeData() {
        photoEditorViewModel.resultUri.observe(viewLifecycleOwner) {
            Log.i(TAG, "resultUri: $it")
            binding.photoEditorView.source.setImageURI(it)
            binding.previewImage.setImageURI(it)
            /*  Glide.with(globalContext).load(it).into(binding.photoEditorView.source)
              Glide.with(globalContext).load(it).into(binding.previewImage)*/
        }
    }

    private fun onClickListeners() {
        binding.apply {
            close.setOnClickListener {
                if (showDiscardDialog) {
                    mainActivity.showUnsavedChangesDialog {
                        popFrom(R.id.backgroundFragment)
                    }
                } else popFrom(R.id.backgroundFragment)
            }
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
                photoEditor.saveAsBitmap(object : OnSaveBitmap {
                    override fun onBitmapReady(saveBitmap: Bitmap) {
                        photoEditorViewModel.bitmapToUri(saveBitmap)?.let {
                            Log.i(TAG, "onBitmapReady: uri: $it")
                            photoEditorViewModel.setUri(it)
                        } ?: {
                            Log.e(TAG, "Error while creating uri")
                        }

                        photoEditorViewModel.hideLoading()
                        popFrom(R.id.backgroundFragment)
                    }
                })
            }
        }
    }

    private fun setupAdapter() {
        adapter = BackgroundAdapter { position, item ->
            binding.photoEditorView.setPaddingDp()
            showDiscardDialog = true
            when (item) {
                is BackgroundItemsModel.SolidColor -> {
                    val resourceId = ColorDrawable(ContextCompat.getColor(requireContext(), item.color))
                    binding.photoEditorView.background = resourceId
                    adapter.setSelection(SOLID_COLOR, position)
                }

                is BackgroundItemsModel.GradientColor -> {
                    val colorsInt = item.colors.map { colorResId -> ContextCompat.getColor(requireContext(), colorResId) }.toIntArray()
                    val gradientDrawable = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colorsInt)
                    binding.photoEditorView.background = gradientDrawable
                    adapter.setSelection(GRADIENT_COLOR, position)
                }

                is BackgroundItemsModel.Pattern -> {
                    binding.photoEditorView.background = ContextCompat.getDrawable(requireContext(), item.resourceId)
                    adapter.setSelection(PATTERN, position)
                }

            }

        }
        binding.recyclerView.adapter = adapter
        adapter.submitList(PhotoViewerUtil.solidColors)
    }

    private fun tabSelectedListener() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let { setListToAdapter(it.position) }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

        })
    }

    private fun setListToAdapter(position: Int) {
        val value = when (position) {
            0 -> PhotoViewerUtil.solidColors
            1 -> PhotoViewerUtil.gradientColors
            else -> PhotoViewerUtil.patternsList
        }
        adapter.submitList(value)
    }
}