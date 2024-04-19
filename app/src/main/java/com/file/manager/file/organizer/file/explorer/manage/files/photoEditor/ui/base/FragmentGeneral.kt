package com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.ui.base

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.firebase.FirebaseUtils.recordException
import com.google.android.datatransport.BuildConfig
import com.google.android.material.snackbar.Snackbar

open class FragmentGeneral : Fragment() {

    private val baseTAG = "BaseTAG"

    protected fun withDelay(delay: Long = 300, block: () -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed(block, delay)
    }

    private fun getResString(stringId: Int): String {
        return context?.resources?.getString(stringId) ?: ""
    }

    /* ---------- Toast ---------- */

    protected fun showToast(message: String) {
        activity?.let {
            try {
                it.runOnUiThread {
                    Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
                }
            } catch (ex: Exception) {
                ex.recordException("showToast : ${it.javaClass.simpleName}")
            }
        }
    }

    protected fun debugToast(message: String) {
        if (BuildConfig.DEBUG) {
            showToast(message)
        }
    }

    protected fun showToast(stringId: Int) {
        val message = getResString(stringId)
        showToast(message)
    }

    /* ---------- Snackbar ---------- */

    protected fun showSnackBar(message: String) {
        this.view?.let { v ->
            activity?.let {
                try {
                    it.runOnUiThread {
                        Snackbar.make(v, message, Snackbar.LENGTH_SHORT).show()
                    }
                } catch (ex: Exception) {
                    ex.recordException("showSnackBar : ${it.javaClass.simpleName}")
                }
            }
        }
    }
}