package com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.ui.base

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.MainActivity
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.di.DIComponent
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.photoViewer.PhotoEditorViewModel
import dev.shreyaspatil.permissionFlow.PermissionFlow
import dev.shreyaspatil.permissionFlow.utils.launch
import dev.shreyaspatil.permissionFlow.utils.registerForPermissionFlowRequestsResult
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.lang.reflect.ParameterizedType


abstract class BaseFragment<VB : ViewBinding> : BaseNavFragment() {

    /**
     * These properties are only valid between onCreateView and onDestroyView
     * @property binding
     *          -> after onCreateView
     *          -> before onDestroyView
     */
    private var _binding: VB? = null
    val binding get() = _binding!!

    private var rootView: View? = null

    val globalContext: Context by lazy { binding.root.context }
    private val globalActivity by lazy { globalContext as Activity }
    val mainActivity by lazy { globalActivity as MainActivity }

    protected val diComponent by lazy { DIComponent() }
    val photoEditorViewModel: PhotoEditorViewModel by activityViewModel()

    private val permissions by lazy { if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) android.Manifest.permission.READ_MEDIA_IMAGES else android.Manifest.permission.READ_EXTERNAL_STORAGE }

    private val permissionFlow by lazy { PermissionFlow.getInstance() }

    private val permissionLauncher by lazy { registerForPermissionFlowRequestsResult() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val type = javaClass.genericSuperclass
        val clazz = (type as ParameterizedType).actualTypeArguments[0] as Class<*>
        val method = clazz.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
        @Suppress("UNCHECKED_CAST")
        _binding = method.invoke(null, layoutInflater, container, false) as VB
        return _binding!!.root
    }

    /**
     *      Use the following method in onViewCreated from escaping reinitializing of views
     *      if (!hasInitializedRootView) {
     *          hasInitializedRootView = true
     *          // Your Code...
     *      }
     */

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        permissionLauncher.launch(permissions)
        observePermissions()
    }

    private fun observePermissions() {
        viewLifecycleOwner.lifecycleScope.launch {
            permissionFlow.getPermissionState(permissions).collect { state ->
                when {
                    state.isGranted -> {
                        //permission granted
                        diComponent.picturesViewModel.fetchPicturesListGroupedByDate()
                    }

                    state.isRationaleRequired == true -> {
                        // permission show rational dialog
                        println("show rational dialog")
                    }

                    else -> {
                        //permission denied
                        println("permission denied")
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        rootView = null
    }
}