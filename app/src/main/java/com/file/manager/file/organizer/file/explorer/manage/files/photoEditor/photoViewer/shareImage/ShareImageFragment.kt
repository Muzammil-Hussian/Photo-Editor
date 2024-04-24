package com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.photoViewer.shareImage

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.R
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.databinding.FragmentShareImageBinding
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.ui.base.BaseFragment

class ShareImageFragment : BaseFragment<FragmentShareImageBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        photoEditorViewModel.resultUri.observe(viewLifecycleOwner) {
            Glide.with(globalContext).load(it).into(binding.image)
        }
    }

    override fun navIconBackPressed() {
        navigateTo(R.id.shareImageFragment, R.id.action_shareImageFragment_to_mainFragment)
    }

    override fun onBackPressed() {
        navigateTo(R.id.shareImageFragment, R.id.action_shareImageFragment_to_mainFragment)
    }
}