package com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.photoViewer.filter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Pair
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.adapter.BaseAdapter
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.databinding.ItemPhotoFiltersImageTextBinding
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.helper.getDiffUtilCallBack
import ja.burhanrashid52.photoeditor.PhotoFilter
import java.io.IOException

class FilterViewAdapter(private val onApplyFilterCallback: (photoFilter: PhotoFilter) -> Unit) : BaseAdapter<Pair<String, PhotoFilter>, FilterViewAdapter.ViewHolder>(getDiffUtilCallBack()) {

    init {
        selectedPosition = 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPhotoFiltersImageTextBinding.inflate(inflater)
        return ViewHolder(binding)

    }

    override fun onBindItem(holder: ViewHolder, item: Pair<String, PhotoFilter>, position: Int) {
        val fromAsset = getBitmapFromAsset(holder.itemView.context, item.first)
        holder.binding.apply {
            image.setImageBitmap(fromAsset)
            name.text = item.second.name.replace("_", " ")
        }
    }

    override fun onItemClick(holder: ViewHolder, item: Pair<String, PhotoFilter>, position: Int) = onApplyFilterCallback.invoke(currentList[position].second)


    inner class ViewHolder(val binding: ItemPhotoFiltersImageTextBinding) : RecyclerView.ViewHolder(binding.root)

    private fun getBitmapFromAsset(context: Context, strName: String): Bitmap? {
        val assetManager = context.assets
        return try {
            val inputStream = assetManager.open(strName)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}
