package com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.photoViewer.adjust

import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.bumptech.glide.Glide
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.R
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.databinding.FragmentPhotoAdjustBinding
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.extensions.beGone
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.extensions.beVisible
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.photoViewer.PhotoViewerUtil
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.photoViewer.dialog.showUnsavedChangesDialog
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.ui.base.AbsLoadingDialog
import com.yalantis.ucrop.UCrop.of
import com.yalantis.ucrop.callback.BitmapCropCallback
import com.yalantis.ucrop.view.TransformImageView
import com.yalantis.ucrop.view.widget.HorizontalProgressWheelView
import java.io.File
import java.util.Locale


private const val TAG = "PhotoAdjustFragmentLogs"

class PhotoAdjustFragment : AbsLoadingDialog<FragmentPhotoAdjustBinding>() {

    private lateinit var adapter: PhotoAdjustAdapter
    private lateinit var uri: Uri
    private lateinit var destination: Uri

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialization()
        observeData()
        setupCropView()
        setupAdapter()
        clickListeners()
    }

    private fun initialization() {
        photoEditorViewModel.showLoading()
    }


    private fun observeData() {
        photoEditorViewModel.resultUri.observe(viewLifecycleOwner) {
            Log.i(TAG, "observeData uri: $it")
            photoEditorViewModel.hideLoading()
            uri = it
            destination = Uri.fromFile(File(globalContext.cacheDir, "SampleImage1.jpg"))
            of(uri, destination)
            binding.image.cropImageView.setImageUri(uri, destination)
            Glide.with(globalContext).load(it).into(binding.previewImage)
        }
    }

    private val mImageListener: TransformImageView.TransformImageListener = object : TransformImageView.TransformImageListener {
        override fun onLoadComplete() {
            Log.i(TAG, "onLoadComplete")
        }

        override fun onLoadFailure(p0: Exception) {
            Log.i(TAG, "onLoadFailure: exception: $p0")
        }

        override fun onRotate(p0: Float) {
            Log.i(TAG, "onRotate: $p0")
        }

        override fun onScale(p0: Float) {
            Log.i(TAG, "onScale: $p0")
        }

        override fun onBrightness(p0: Float) {
            Log.i(TAG, "onBrightness: $p0")
            binding.rulerCurrentValue.text = String.format(Locale.getDefault(), "%d", p0.toInt())
        }

        override fun onContrast(p0: Float) {
            Log.i(TAG, "onContrast: $p0")
            binding.rulerCurrentValue.text = String.format(Locale.getDefault(), "%d", p0.toInt())
        }

        override fun onSaturation(p0: Float) {
            Log.i(TAG, "onSaturation: $p0")
            binding.rulerCurrentValue.text = String.format(Locale.getDefault(), "%d", p0.toInt())
        }

        override fun onSharpness(p0: Float) {
            Log.i(TAG, "onSharpness: $p0")
            binding.rulerCurrentValue.text = String.format(Locale.getDefault(), "%d", p0.toInt())
        }
    }

    private fun setupCropView() {
        binding.image.apply {
            cropImageView.apply {
                isRotateEnabled = false
                isGestureEnabled = false
                isScaleEnabled = false
                setTransformImageListener(mImageListener)
                rulerChangeListener(0)
            }

            overlayView.apply {
                setupCropBounds()
                setShowCropFrame(false)
                setShowCropGrid(false)
                setDimmedColor(Color.TRANSPARENT)
            }

        }
    }

    private fun setupAdapter() {
        with(binding) {
            adapter = PhotoAdjustAdapter {
                image.cropImageView.apply {
                    val value: Int = when (PhotoAdjustAdapter.selectedPosition) {
                        0 -> currentBrightness.toInt()
                        1 -> currentContrast.toInt()
                        2 -> currentSaturation.toInt()
                        3 -> currentSharpness.toInt()
                        else -> currentBrightness.toInt()
                    }

                    rulerCurrentValue.text = value.toString()

                    rulerChangeListener(it)
                }
            }
            recyclerView.adapter = adapter
            adapter.submitList(PhotoViewerUtil.adjustPhotoOptionsList)
        }
    }

    private fun rulerChangeListener(position: Int) {
        val gestureCropImageView = binding.image.cropImageView
        binding.ruler.setScrollingListener(object : HorizontalProgressWheelView.ScrollingListener {
            override fun onScrollStart() {
                gestureCropImageView.cancelAllAnimations()
            }

            override fun onScroll(value: Float, totalDistance: Float) {
                gestureCropImageView.apply {
                    cancelAllAnimations()
                    when (position) {
                        0 -> postBrightness(value / BRIGHTNESS_WIDGET_SENSITIVITY_COEFFICIENT)
                        1 -> postContrast(value / CONTRAST_WIDGET_SENSITIVITY_COEFFICIENT)
                        2 -> postSaturation(value / SATURATION_WIDGET_SENSITIVITY_COEFFICIENT)
                        3 -> postSharpness(value / SHARPNESS_WIDGET_SENSITIVITY_COEFFICIENT)
                    }
                    setImageToWrapCropBounds()
                }
            }

            override fun onScrollEnd() {
                gestureCropImageView.setImageToWrapCropBounds()
            }
        })
    }


    private fun clickListeners() {
        binding.apply {
            close.setOnClickListener { mainActivity.showUnsavedChangesDialog { popFrom(R.id.photoAdjustFragment) } }
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
                binding.image.cropImageView.cropAndSaveImage(
                    DEFAULT_COMPRESS_FORMAT,
                    DEFAULT_COMPRESS_QUALITY,
                    object : BitmapCropCallback {
                        override fun onBitmapCropped(
                            resultUri: Uri,
                            offsetX: Int,
                            offsetY: Int,
                            imageWidth: Int,
                            imageHeight: Int
                        ) {
                            Log.i(TAG, "onBitmapCropped: \n uri: $resultUri \n offsetX: $offsetX \n offsetY: $offsetY \n imageWidth: $imageWidth \n imageHeight: $imageHeight")
                            photoEditorViewModel.setUri(resultUri)
                            photoEditorViewModel.hideLoading()
                            popFrom(R.id.photoAdjustFragment)
                        }

                        override fun onCropFailure(t: Throwable) {
                            Log.i(TAG, "onCropFailure: throwable: ${t.message} ")
                        }
                    })
            }
        }
    }

    override fun onStop() {
        super.onStop()
        binding.image.cropImageView.cancelAllAnimations()
    }

    companion object {
        private const val DEFAULT_COMPRESS_QUALITY = 90
        val DEFAULT_COMPRESS_FORMAT = Bitmap.CompressFormat.JPEG
        private const val BRIGHTNESS_WIDGET_SENSITIVITY_COEFFICIENT = 3
        private const val CONTRAST_WIDGET_SENSITIVITY_COEFFICIENT = 4
        private const val SATURATION_WIDGET_SENSITIVITY_COEFFICIENT = 3
        private const val SHARPNESS_WIDGET_SENSITIVITY_COEFFICIENT = 400

    }
}

