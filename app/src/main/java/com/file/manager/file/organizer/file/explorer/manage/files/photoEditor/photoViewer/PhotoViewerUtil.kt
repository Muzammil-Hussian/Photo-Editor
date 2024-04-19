package com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.photoViewer

import android.util.Pair
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.R
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.photoViewer.addText.ColorModel
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.photoViewer.background.BackgroundItemsModel
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.photoViewer.model.PhotoViewerModel
import ja.burhanrashid52.photoeditor.PhotoFilter

const val SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage.jpg"

object PhotoViewerUtil {

    val photoEditorList = arrayListOf(
        PhotoViewerModel(R.drawable.ic_crop, R.string.crop),
        PhotoViewerModel(R.drawable.ic_rotate_right, R.string.rotate),
        PhotoViewerModel(R.drawable.ic_adjust_hue, R.string.adjust),
        PhotoViewerModel(R.drawable.ic_filters, R.string.filter),
        PhotoViewerModel(R.drawable.ic_add_text, R.string.text),
        PhotoViewerModel(R.drawable.ic_draw, R.string.draw),
        PhotoViewerModel(R.drawable.ic_background, R.string.background),
    )
    val cropRatioList = arrayListOf(
        PhotoViewerModel(R.drawable.ic_ration_custom, R.string.ratio_custom),
        PhotoViewerModel(R.drawable.ic_ratio_1_1, R.string.ratio_1_1),
        PhotoViewerModel(R.drawable.ic_ratio_4_3, R.string.ratio_4_3),
        PhotoViewerModel(R.drawable.ic_ratio_16_9, R.string.ratio_16_9),
        PhotoViewerModel(R.drawable.ic_ratio_3_2, R.string.ratio_3_2),
        PhotoViewerModel(R.drawable.ic_ratio_9_16, R.string.ratio_9_16),
        PhotoViewerModel(R.drawable.ic_ratio_2_3, R.string.ratio_2_3),
        PhotoViewerModel(R.drawable.ic_ratio_5_4, R.string.ratio_5_4),
        PhotoViewerModel(R.drawable.ic_ratio_4_5, R.string.ratio_4_5),
        PhotoViewerModel(R.drawable.ic_ratio_3_1, R.string.ratio_3_1),
        PhotoViewerModel(R.drawable.ic_ratio_3_4, R.string.ratio_3_4)
    )

    val rotatePhotoList = arrayListOf(
        PhotoViewerModel(R.drawable.ic_rotate_left, R.string.rotate_left),
        PhotoViewerModel(R.drawable.ic_rotate_right, R.string.rotate_right),
        PhotoViewerModel(R.drawable.ic_flip_vertical, R.string.vertical),
        PhotoViewerModel(R.drawable.ic_flip_horizontal, R.string.horizontal),
    )

    val  adjustPhotoOptionsList = arrayListOf(
        PhotoViewerModel(R.drawable.ic_adjust_brightness, R.string.brightness),
        PhotoViewerModel(R.drawable.ic_adjust_contrast, R.string.contrast),
//        PhotoViewerModel(R.drawable.ic_adjust_hue, R.string.hue),
        PhotoViewerModel(R.drawable.ic_adjust_saturation, R.string.saturation),
        PhotoViewerModel(R.drawable.ic_adjust_sharpen, R.string.sharpen),
    )

    val colorsList = arrayListOf(
        ColorModel(R.color.indigo_200),
        ColorModel(R.color.purple),
        ColorModel(R.color.green_bright_lime),
        ColorModel(R.color.green_dark),
        ColorModel(R.color.black),
        ColorModel(R.color.white),
        ColorModel(R.color.gentle_sky_blue),
        ColorModel(R.color.outline_color),
        ColorModel(R.color.surface_grey),
        ColorModel(R.color.light_red),
        ColorModel(R.color.textColorSecondary),
        ColorModel(R.color.yellow),
        ColorModel(R.color.brown),
        ColorModel(R.color.orange_vibrant),
        ColorModel(R.color.red_bright_coral),
        ColorModel(R.color.red_400),
        ColorModel(R.color.red_700),
        ColorModel(R.color.light_blue_200),
        ColorModel(R.color.light_blue_700),
        ColorModel(R.color.primaryColor),
        ColorModel(R.color.indigo_500),
        ColorModel(R.color.indigo_800)
    )

    val solidColors = listOf(
        BackgroundItemsModel.SolidColor(R.color.indigo_200),
        BackgroundItemsModel.SolidColor(R.color.purple),

        BackgroundItemsModel.SolidColor(R.color.green_bright_lime),
        BackgroundItemsModel.SolidColor(R.color.green_dark),

        BackgroundItemsModel.SolidColor(R.color.yellow),
        BackgroundItemsModel.SolidColor(R.color.brown),

        BackgroundItemsModel.SolidColor(R.color.light_blue_200),
        BackgroundItemsModel.SolidColor(R.color.light_blue_700),
        BackgroundItemsModel.SolidColor(R.color.primaryColor),
        BackgroundItemsModel.SolidColor(R.color.indigo_500),
        BackgroundItemsModel.SolidColor(R.color.indigo_800),

        BackgroundItemsModel.SolidColor(R.color.white),
        BackgroundItemsModel.SolidColor(R.color.gentle_sky_blue),
        BackgroundItemsModel.SolidColor(R.color.outline_color),
        BackgroundItemsModel.SolidColor(R.color.surface_grey),
        BackgroundItemsModel.SolidColor(R.color.light_red),
        BackgroundItemsModel.SolidColor(R.color.textColorSecondary),
        BackgroundItemsModel.SolidColor(R.color.black),

        BackgroundItemsModel.SolidColor(R.color.orange_vibrant),
        BackgroundItemsModel.SolidColor(R.color.red_bright_coral),
        BackgroundItemsModel.SolidColor(R.color.red_400),
        BackgroundItemsModel.SolidColor(R.color.red_700)
    )

    val gradientColors = arrayListOf(
        BackgroundItemsModel.GradientColor(intArrayOf(R.color.white, R.color.white)),
        BackgroundItemsModel.GradientColor(intArrayOf(R.color.grey_gradient_start, R.color.grey_gradient_end)),
        BackgroundItemsModel.GradientColor(intArrayOf(R.color.red_gradient_start, R.color.red_gradient_end)),
        BackgroundItemsModel.GradientColor(intArrayOf(R.color.yellow_gradient_start, R.color.yellow_gradient_end)),
        BackgroundItemsModel.GradientColor(intArrayOf(R.color.blue_gradient_start, R.color.blue_gradient_end)),
        BackgroundItemsModel.GradientColor(intArrayOf(R.color.purple_gradient_start, R.color.purple_gradient_end)),
        BackgroundItemsModel.GradientColor(intArrayOf(R.color.green_gradient_start, R.color.green_gradient_end)),
        BackgroundItemsModel.GradientColor(intArrayOf(R.color.red_dark_gradient_start, R.color.red_dark_gradient_end)),
        BackgroundItemsModel.GradientColor(intArrayOf(R.color.orange_gradient_start, R.color.orange_gradient_end)),
        BackgroundItemsModel.GradientColor(intArrayOf(R.color.light_blue_gradient_start, R.color.light_blue_gradient_end)),
        BackgroundItemsModel.GradientColor(intArrayOf(R.color.white_gradient_start, R.color.white_gradient_end))
    )

    val patternsList = listOf(
        BackgroundItemsModel.Pattern(R.drawable.bg_1),
        BackgroundItemsModel.Pattern(R.drawable.bg_2),
        BackgroundItemsModel.Pattern(R.drawable.bg_3),
        BackgroundItemsModel.Pattern(R.drawable.bg_4),
        BackgroundItemsModel.Pattern(R.drawable.bg_5),
        BackgroundItemsModel.Pattern(R.drawable.bg_6),
        BackgroundItemsModel.Pattern(R.drawable.bg_7),
        BackgroundItemsModel.Pattern(R.drawable.bg_8),
        BackgroundItemsModel.Pattern(R.drawable.bg_9),
        BackgroundItemsModel.Pattern(R.drawable.bg_10),
        BackgroundItemsModel.Pattern(R.drawable.bg_11),
        BackgroundItemsModel.Pattern(R.drawable.bg_12),
        BackgroundItemsModel.Pattern(R.drawable.bg_13),
        BackgroundItemsModel.Pattern(R.drawable.bg_14),
        BackgroundItemsModel.Pattern(R.drawable.bg_15),
        BackgroundItemsModel.Pattern(R.drawable.bg_16),
        BackgroundItemsModel.Pattern(R.drawable.bg_17),
        BackgroundItemsModel.Pattern(R.drawable.bg_18),
        BackgroundItemsModel.Pattern(R.drawable.bg_19),
        BackgroundItemsModel.Pattern(R.drawable.bg_20),
        BackgroundItemsModel.Pattern(R.drawable.bg_21),
        BackgroundItemsModel.Pattern(R.drawable.bg_22),
        BackgroundItemsModel.Pattern(R.drawable.bg_23),
        BackgroundItemsModel.Pattern(R.drawable.bg_24),
        BackgroundItemsModel.Pattern(R.drawable.bg_25),
        BackgroundItemsModel.Pattern(R.drawable.bg_26),
        BackgroundItemsModel.Pattern(R.drawable.bg_27),
        BackgroundItemsModel.Pattern(R.drawable.bg_28),
        BackgroundItemsModel.Pattern(R.drawable.bg_29),
        BackgroundItemsModel.Pattern(R.drawable.bg_30),
        BackgroundItemsModel.Pattern(R.drawable.bg_31),
        BackgroundItemsModel.Pattern(R.drawable.bg_32),
        BackgroundItemsModel.Pattern(R.drawable.bg_33),
        BackgroundItemsModel.Pattern(R.drawable.bg_34),
        BackgroundItemsModel.Pattern(R.drawable.bg_35)
    )


    val filtersPairList: List<Pair<String, PhotoFilter>> = listOf(
        Pair("filters/original.jpg", PhotoFilter.NONE),
        Pair("filters/auto_fix.png", PhotoFilter.AUTO_FIX),
        Pair("filters/brightness.png", PhotoFilter.BRIGHTNESS),
        Pair("filters/contrast.png", PhotoFilter.CONTRAST),
        Pair("filters/documentary.png", PhotoFilter.DOCUMENTARY),
        Pair("filters/dual_tone.png", PhotoFilter.DUE_TONE),
        Pair("filters/fill_light.png", PhotoFilter.FILL_LIGHT),
        Pair("filters/fish_eye.png", PhotoFilter.FISH_EYE),
        Pair("filters/grain.png", PhotoFilter.GRAIN),
        Pair("filters/gray_scale.png", PhotoFilter.GRAY_SCALE),
        Pair("filters/lomish.png", PhotoFilter.LOMISH),
        Pair("filters/negative.png", PhotoFilter.NEGATIVE),
        Pair("filters/posterize.png", PhotoFilter.POSTERIZE),
        Pair("filters/saturate.png", PhotoFilter.SATURATE),
        Pair("filters/sepia.png", PhotoFilter.SEPIA),
        Pair("filters/sharpen.png", PhotoFilter.SHARPEN),
        Pair("filters/temprature.png", PhotoFilter.TEMPERATURE),
        Pair("filters/tint.png", PhotoFilter.TINT),
        Pair("filters/vignette.png", PhotoFilter.VIGNETTE),
        Pair("filters/cross_process.png", PhotoFilter.CROSS_PROCESS),
        Pair("filters/b_n_w.png", PhotoFilter.BLACK_WHITE),
        Pair("filters/flip_horizental.png", PhotoFilter.FLIP_HORIZONTAL),
        Pair("filters/flip_vertical.png", PhotoFilter.FLIP_VERTICAL),
        Pair("filters/rotate.png", PhotoFilter.ROTATE)
    )
}