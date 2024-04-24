package com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.ui.base

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navOptions
import androidx.viewbinding.ViewBinding
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.MainActivity
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.R
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.di.DIComponent
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.photoViewer.PhotoEditorViewModel
import dev.shreyaspatil.permissionFlow.PermissionFlow
import dev.shreyaspatil.permissionFlow.utils.registerForPermissionFlowRequestsResult
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.lang.reflect.ParameterizedType

private const val TAG = "BaseFragmentLogs"

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

    private val permissions by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES)
        else arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

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

            permissionFlow.getMultiplePermissionState(*permissions).collect { state ->


                // Check whether all permissions are granted
                val allGranted = state.allGranted

                // List of granted permissions
                val grantedPermissions = state.grantedPermissions

                // List of denied permissions
                val deniedPermissions = state.deniedPermissions

                // List of permissions requiring rationale
                val permissionsRequiringRationale = state.permissionsRequiringRationale

                Log.i(TAG, "observePermissions: allGranted: $allGranted")
                Log.i(TAG, "observePermissions: granterPermissionList: $grantedPermissions")
                Log.i(TAG, "observePermissions: deniedPermissionsList: $deniedPermissions")
                Log.i(TAG, "observePermissions: permissionRequiredRationale: $permissionsRequiringRationale")
                when {
                    state.allGranted -> {
                        //permission granted
                        diComponent.picturesViewModel.fetchPicturesListGroupedByDate()
                    }

                    else -> {
                        //permission denied
                        Log.i(TAG, "observePermissions: permission denied")
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