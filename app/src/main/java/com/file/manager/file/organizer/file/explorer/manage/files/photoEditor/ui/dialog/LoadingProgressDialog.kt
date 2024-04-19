package com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.databinding.DialogLoadingProgressBinding

class LoadingProgressDialog(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.requestFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)
        val binding = DialogLoadingProgressBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun showLoadingDialog() {
        show()
    }

    fun hideLoadingDialog() {
        if (isShowing) dismiss()
    }
}
