package com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.photoViewer.addText

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.R
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.databinding.FragmentAddTextBinding
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.extensions.beGone
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.extensions.beVisible
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.extensions.focusAndShowKeyboard
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.extensions.hideKeyboard
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.photoViewer.PhotoViewerUtil
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.photoViewer.dialog.showUnsavedChangesDialog
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.ui.base.BaseFragment
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.ui.dialog.LoadingProgressDialog
import ja.burhanrashid52.photoeditor.OnPhotoEditorListener
import ja.burhanrashid52.photoeditor.OnSaveBitmap
import ja.burhanrashid52.photoeditor.PhotoEditor
import ja.burhanrashid52.photoeditor.TextStyleBuilder
import ja.burhanrashid52.photoeditor.ViewType
import ja.burhanrashid52.photoeditor.selectedRootView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


private const val TAG = "AddTextFragmentLogs"

class AddTextFragment : BaseFragment<FragmentAddTextBinding>() {

    private val dialog by lazy { LoadingProgressDialog(globalContext) }

    private lateinit var adapter: AddTextAdapter
    private val photoEditor by lazy { PhotoEditor.Builder(requireContext(), binding.photoEditorView).setPinchTextScalable(true).build() }
    private var colorCode: Int = Color.WHITE

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        setupPhotoViewer()
        setupAdapter()
        clickListeners()
    }


    private fun observeData() {
        photoEditorViewModel.resultUri.observe(viewLifecycleOwner) {
            binding.photoEditorView.source.setImageURI(it)
            binding.previewImage.setImageURI(it)
        }
    }

    override fun navIconBackPressed() = hideKeyboardOrPopFragment()

    private fun hideKeyboardOrPopFragment() {
        if (binding.addTextEditText.hasFocus()) activity?.hideKeyboard()
        else photoEditor.apply {
            if (!undo() && !redo()) popFrom(R.id.addTextFragment)
            else activity?.showUnsavedChangesDialog { popFrom(R.id.addTextFragment) }
        }
    }

    private fun clickListeners() {
        binding.apply {

            addNewText.setOnClickListener {
                addTextEditText.apply {
                    beVisible()
                    focusAndShowKeyboard()
                }
                addNewText.beGone()
            }

            close.setOnClickListener { hideKeyboardOrPopFragment() }
            undo.setOnClickListener { photoEditor.undo() }
            redo.setOnClickListener { photoEditor.redo() }

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

                if (!binding.addTextEditText.hasFocus()) {
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
                                popFrom(R.id.addTextFragment)
                            }
                        }
                    })
                } else {
                    val styleBuilder = TextStyleBuilder()
                    styleBuilder.withTextColor(colorCode)
                    if (binding.addTextEditText.text.isEmpty()) {
                        showToast("Add something...")
                        return@setOnClickListener
                    }
                    photoEditor.addText(binding.addTextEditText.text.toString(), styleBuilder)
                }

                addNewText.beVisible()

                addTextEditText.apply {
                    activity?.hideKeyboard()
                    beGone()
                }
            }
        }
    }

    private fun setupPhotoViewer() {

        photoEditor.setOnPhotoEditorListener(object : OnPhotoEditorListener {
            override fun onAddViewListener(viewType: ViewType, numberOfAddedViews: Int) {
                Log.i(TAG, "onAddViewListener: ")
            }

            override fun onEditTextChangeListener(rootView: View, text: String, colorCode: Int) {
                Log.i(
                    TAG,
                    "onEditTextChangeListener: rootView:$rootView,text: $text, colorCode: $colorCode "
                )
                binding.apply {
                    addNewText.beGone()
                    addTextEditText.apply {
                        focusAndShowKeyboard()
                        beVisible()
                        this.setText(text)
                    }
                }
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

    private fun setupAdapter() {
        adapter = AddTextAdapter { _, item ->
            colorCode = ContextCompat.getColor(requireContext(), item.color)
            val styleBuilder = TextStyleBuilder()
            styleBuilder.withTextColor(colorCode)
            if (binding.addTextEditText.hasFocus()) binding.addTextEditText.setTextColor(colorCode)
            else selectedRootView?.let { photoEditor.editText(it, it.findViewById<TextView>(ja.burhanrashid52.photoeditor.R.id.tvPhotoEditorText).text.toString(), styleBuilder) }
        }
        binding.recyclerView.adapter = adapter
        adapter.submitList(PhotoViewerUtil.colorsList)
    }
}