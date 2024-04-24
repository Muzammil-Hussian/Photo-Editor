package com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.ui.base

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withCreated
import androidx.lifecycle.withResumed
import androidx.lifecycle.withStarted
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.R
import kotlinx.coroutines.launch

abstract class BaseNavFragment : FragmentGeneral() {

    private var callback: OnBackPressedCallback? = null
    val navOptions by lazy {
        navOptions {
            launchSingleTop = false
            anim {
                enter = R.anim.fragment_open_enter
                exit = R.anim.fragment_open_exit
                popEnter = R.anim.fragment_close_enter
                popExit = R.anim.fragment_close_exit
            }
        }
    }

    /**
     *  @since : Write Code for BackPress Functionality
     */
    override fun onResume() {
        super.onResume()
        callback?.remove()
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
                this.remove()
            }
        }.also {
            (context as FragmentActivity).onBackPressedDispatcher.addCallback(this, it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                navIconBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    open fun navIconBackPressed() {
        onBackPressed()
    }

    open fun onBackPressed() {
        findNavController().currentDestination?.let { popFrom(it.id) }
    }

    /**
     *     Used launchWhenCreated, bcz of screen rotation
     *     Used launchWhenResumed, bcz of screen rotation
     * @param fragmentId : Current Fragment's Id (from Nav Graph)
     * @param action : Action / Id of other fragment
     * @param bundle : Pass bundle as a NavArgs to destination.
     */

    protected fun navigateTo(fragmentId: Int, action: Int, bundle: Bundle) {
        launchWhenCreated {
            if (isAdded && isCurrentDestination(fragmentId)) {
                findNavController().navigate(action, bundle)
            }
        }
    }

    protected fun navigateTo(fragmentId: Int, action: Int) {
        launchWhenCreated {
            if (isAdded && isCurrentDestination(fragmentId)) {
                findNavController().navigate(action, null, navOptions = navOptions)
            }
        }
    }

    protected fun navigateTo(fragmentId: Int, action: NavDirections) {
        launchWhenCreated {
            if (isAdded && isCurrentDestination(fragmentId)) {
                findNavController().navigate(action)
            }
        }
    }

    protected fun popFrom(fragmentId: Int) {
        launchWhenCreated {
            if (isAdded && isCurrentDestination(fragmentId)) {
                findNavController().popBackStack()
            }
        }
    }

    protected fun popFrom(fragmentId: Int, destinationFragmentId: Int, inclusive: Boolean = false) {
        launchWhenCreated {
            if (isAdded && isCurrentDestination(fragmentId)) {
                findNavController().popBackStack(destinationFragmentId, inclusive)
            }
        }
    }

    private fun isCurrentDestination(fragmentId: Int): Boolean {
        return findNavController().currentDestination?.id == fragmentId
    }

    private fun launchWhenCreated(callback: () -> Unit) {
        lifecycleScope.launch { lifecycle.withCreated(callback) }
    }

    protected fun launchWhenStarted(callback: () -> Unit) {
        lifecycleScope.launch { lifecycle.withStarted(callback) }
    }

    protected fun launchWhenResumed(callback: () -> Unit) {
        lifecycleScope.launch { lifecycle.withResumed(callback) }
    }
}