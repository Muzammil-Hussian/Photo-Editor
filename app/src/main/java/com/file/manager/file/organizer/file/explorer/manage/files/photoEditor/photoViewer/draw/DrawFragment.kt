package com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.photoViewer.draw

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.R
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.databinding.FragmentDrawBinding
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.photoViewer.PhotoViewerUtil
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.photoViewer.addText.AddTextAdapter
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.ui.base.BaseFragment
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.ui.dialog.LoadingProgressDialog
import com.google.android.material.slider.Slider
import ja.burhanrashid52.photoeditor.OnPhotoEditorListener
import ja.burhanrashid52.photoeditor.OnSaveBitmap
import ja.burhanrashid52.photoeditor.PhotoEditor
import ja.burhanrashid52.photoeditor.ViewType
import ja.burhanrashid52.photoeditor.shape.ShapeBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "DrawFragmentLogs"

class DrawFragment : BaseFragment<FragmentDrawBinding>() {

    private lateinit var adapter: AddTextAdapter

    private lateinit var photoEditor: PhotoEditor

    private var colorCode: Int = Color.WHITE

    private val shapeBuilder: ShapeBuilder by lazy { ShapeBuilder().withShapeColor(Color.WHITE).withShapeSize(17F) }
    private val dialog by lazy { LoadingProgressDialog(globalContext) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickListeners()
        observeData()
        setupPhotoViewer()
        setupBrushDraw()
        setupAdapter()
        sliderTouchListener()
    }

    private fun clickListeners() {
        binding.apply {
            undo.setOnClickListener { photoEditor.undo() }
            redo.setOnClickListener { photoEditor.redo() }

            done.setOnClickListener {
                dialog.showLoadingDialog()
                photoEditor.saveAsBitmap(object : OnSaveBitmap {
                    override fun onBitmapReady(saveBitmap: Bitmap) {
                        Log.i(TAG, "onBitmapReady: $saveBitmap")
                        lifecycleScope.launch {
                            delay(800)
                            photoEditorViewModel.bitmapToUri(saveBitmap)?.let {
                                Log.i(TAG, "onBitmapReady: uri: $it")
                                photoEditorViewModel.setUri(it)
                            } ?: {
                                Log.e(TAG, "Error while creating uri")
                            }

                            dialog.hideLoadingDialog()
                            popFrom(R.id.drawFragment)
                        }
                    }
                })
            }
        }
    }

    private fun observeData() {
        photoEditorViewModel.resultUri.observe(viewLifecycleOwner) {
//            Glide.with(globalContext).load(it).into(binding.photoEditorView.source)
            binding.photoEditorView.source.setImageURI(it)
        }
    }

    private fun setupPhotoViewer() {
        val mTextSwitzerTf = ResourcesCompat.getFont(globalContext, R.font.switzer_medium)

        photoEditor = PhotoEditor.Builder(globalContext, binding.photoEditorView).setPinchTextScalable(true).setClipSourceImage(true).setDefaultTextTypeface(mTextSwitzerTf).build()

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

        binding.photoEditorView.source.scaleType = ImageView.ScaleType.FIT_XY

    }

    private fun setupBrushDraw() {
        photoEditor.apply {
            setBrushDrawingMode(true)
            setShape(shapeBuilder)
        }
    }

    private fun sliderTouchListener() {
        binding.slider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {}

            override fun onStopTrackingTouch(slider: Slider) {
                photoEditor.setShape(shapeBuilder.withShapeSize(slider.value))
            }
        })
    }

    private fun setupAdapter() {
        adapter = AddTextAdapter { _, item ->
            colorCode = ContextCompat.getColor(globalContext, item.color)
            photoEditor.setShape(shapeBuilder.withShapeColor(colorCode))
        }

        binding.recyclerView.adapter = adapter
        Log.i(TAG, "setupAdapter: ${PhotoViewerUtil.colorsList}")
        adapter.submitList(PhotoViewerUtil.colorsList)
    }
}