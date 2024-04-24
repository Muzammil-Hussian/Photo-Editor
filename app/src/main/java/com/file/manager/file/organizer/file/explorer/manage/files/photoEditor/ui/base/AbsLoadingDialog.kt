package com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.ui.base

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.viewbinding.ViewBinding
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.ui.dialog.LoadingProgressDialog

private const val TAG = "AbsLoadingDialogLogs"

abstract class AbsLoadingDialog<VB : ViewBinding> : BaseFragment<VB>() {
    private var loadingProgressDialog: LoadingProgressDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLoadingState()
    }


    override fun onStart() {
        super.onStart()
        observeLoadingState()
    }

    private fun observeLoadingState() {
        photoEditorViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            Log.i(TAG, "observeLoadingState: loading: $isLoading")
            if (isLoading) {
                showLoadingDialog()
            } else {
                hideLoadingDialog()
            }
        }
    }

    private fun showLoadingDialog() {
        Log.i(TAG, "showLoadingDialog")
        if (loadingProgressDialog == null) {
            loadingProgressDialog = LoadingProgressDialog(requireContext())
        }
        loadingProgressDialog?.showLoadingDialog()
    }

    private fun hideLoadingDialog() {
        Log.i(TAG, "hideLoadingDialog")
        if (loadingProgressDialog?.isShowing == true) loadingProgressDialog?.hideLoadingDialog()
    }

    override fun onDestroyView() {
        loadingProgressDialog?.dismiss()
        loadingProgressDialog = null
        super.onDestroyView()
    }
}