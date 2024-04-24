package com.file.manager.file.organizer.file.explorer.manage.files.photoEditor

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.adapter.PhotosAdapter
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.adapter.folderDataType
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.componentView.recyclerView.listeners.MyZoomListener
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.componentView.recyclerView.manager.MyCustomGridManager
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.databinding.FragmentMainBinding
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.ui.base.AbsLoadingDialog

private const val TAG = "MainFragmentLogs"
const val MAX_COLUMN_COUNT = 6

class MainFragment : AbsLoadingDialog<FragmentMainBinding>() {

    private lateinit var adapter: PhotosAdapter

    private var mediaColumnCnt = 3
    private var mZoomListener: MyZoomListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        photoEditorViewModel.showLoading()
        setupAdapter()
        diComponent.picturesViewModel.groupedPicturesByDateUiState.observe(viewLifecycleOwner) {
            Log.i(TAG, "onViewCreated: groupedPictures $it")
            adapter.setData(it)
            photoEditorViewModel.hideLoading()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        (activity as MainActivity).finishAndRemoveTask()
    }

    private fun setupAdapter() {
        adapter = PhotosAdapter()
        binding.recyclerView.apply {
            layoutManager = MyCustomGridManager(globalContext, mediaColumnCnt)
            adapter = this@MainFragment.adapter
            (layoutManager as? MyCustomGridManager)?.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int = if (this@MainFragment.adapter.getItemViewType(position) == folderDataType) mediaColumnCnt else 1
            }
            initZoomListener()
            setupZoomListener(mZoomListener)
        }

        adapter.callBackData = { position, listItem ->
            Log.i(TAG, "callbackData: position: $position, listItem: $listItem")
            photoEditorViewModel.fetchUriFromMediaStorePath(listItem.path)
            Log.i(TAG, "setupAdapter: ${listItem.path}")
            navigateTo(R.id.mainFragment, R.id.action_mainFragment_to_photoEditorFragment)

        }

        adapter.selectedListCallback = { selectedListSize, newSelectedList, position, listItem ->
            Log.i(TAG, "selectedListSize $selectedListSize")
            Log.i(TAG, "newSelectedList: $newSelectedList")
            Log.i(TAG, "position: $position")
            Log.i(TAG, "listItem: $listItem")
        }
    }

    private fun initZoomListener() {
        val layoutManager = binding.recyclerView.layoutManager as MyCustomGridManager
        mZoomListener = object : MyZoomListener {
            override fun zoomIn() {
                if (layoutManager.spanCount > 1) {
                    reduceColumnCount()
                }
                showToast("zoom in")
                Log.d("zoomIn()", "Zoom in")
            }

            override fun zoomOut() {
                if (layoutManager.spanCount < MAX_COLUMN_COUNT) {
                    increaseColumnCount()
                }
                showToast("zoom out")
                Log.d("zoomOut()", "Zoom out")
            }
        }
    }

    private fun increaseColumnCount() {
        mediaColumnCnt += 1
        columnCountChanged()
    }

    private fun reduceColumnCount() {
        mediaColumnCnt -= 1
        columnCountChanged()
    }

    private fun columnCountChanged() {
        (binding.recyclerView.layoutManager as MyCustomGridManager).spanCount = mediaColumnCnt
        adapter.apply {
            notifyItemRangeChanged(0, adapter.itemCount)
        }
    }
}

