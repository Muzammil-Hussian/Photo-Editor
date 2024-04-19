package com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.repository

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.WorkerThread
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.extensions.formatDate
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.extensions.getFilenameFromPath
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.extensions.getLongValue
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.extensions.getStringValue
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.extensions.queryCursor
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.model.DateGroupedItems
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.model.ListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


interface PicturesRepository {
    @WorkerThread
    suspend fun picturesListGroupedByDate(): ArrayList<DateGroupedItems>
}

private const val TAG = "PicturesRepositoryLogs"

class PicturesRepositoryImpl(private val context: Context) : PicturesRepository {

    override suspend fun picturesListGroupedByDate(): ArrayList<DateGroupedItems> {
        val dateGroupMap = LinkedHashMap<String, ArrayList<ListItem>>()

        getMediaItems(false).collectLatest { listItems ->
            for (item in listItems) {
                val dateGroup = formatDate(context, item.modified)
                if (dateGroupMap.containsKey(dateGroup)) {
                    dateGroupMap[dateGroup]?.add(item)
                } else {
                    dateGroupMap[dateGroup] = arrayListOf(item)
                }
            }
        }

        val dateGroupList = ArrayList<DateGroupedItems>()
        for ((date, images) in dateGroupMap) {
            dateGroupList.add(DateGroupedItems(date, images))
        }

        Log.i(TAG, "getVideosListGroupedByDate: $dateGroupList")

        return dateGroupList
    }

    private suspend fun getMediaItems(showHidden: Boolean = false): Flow<ArrayList<ListItem>> = flow {
        val listItems = ArrayList<ListItem>()
        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(
            MediaStore.MediaColumns.DATA,
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns.SIZE,
            MediaStore.MediaColumns.DATE_MODIFIED
        )

        val sortOrder = "${MediaStore.MediaColumns.DATE_MODIFIED} DESC "


        context.queryCursor(uri, projection, sortOrder = sortOrder) { cursor ->
            val path = cursor.getStringValue(MediaStore.MediaColumns.DATA)
            val name = cursor.getStringValue(MediaStore.MediaColumns.DISPLAY_NAME) ?: path.getFilenameFromPath()

            if (!showHidden && name.startsWith(".")) return@queryCursor

            val size = cursor.getLongValue(MediaStore.MediaColumns.SIZE)
            if (size == 0L) return@queryCursor

            val lastModified = cursor.getLongValue(MediaStore.MediaColumns.DATE_MODIFIED) * 1000

            listItems.add(ListItem(path, name, false, 0, size, lastModified))

        }
        emit(listItems)

    }.flowOn(Dispatchers.IO)


}