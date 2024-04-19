package com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.photoViewer.dialog

import android.app.Activity
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.databinding.DialogUnSavedChangesBinding
import com.google.android.material.bottomsheet.BottomSheetDialog


fun Activity.showUnsavedChangesDialog(leaveCallback: () -> Unit) {
    val bottomSheetDialog = BottomSheetDialog(this)
    val bottomSheetBinding = DialogUnSavedChangesBinding.inflate(layoutInflater)
    bottomSheetDialog.setContentView(bottomSheetBinding.root)

    bottomSheetBinding.apply {
        leave.setOnClickListener {
            bottomSheetDialog.dismiss()
            leaveCallback.invoke()
        }

        keepEditing.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
    }

    bottomSheetDialog.show()
}