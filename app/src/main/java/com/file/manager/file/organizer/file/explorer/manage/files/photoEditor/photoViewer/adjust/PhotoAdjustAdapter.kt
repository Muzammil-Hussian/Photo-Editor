package com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.photoViewer.adjust

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.photoViewer.model.PhotoViewerModel
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.R
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.databinding.ItemImageTextBinding
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.extensions.setColor
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.extensions.setSvgColor
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.helper.getDiffUtilCallBack


class PhotoAdjustAdapter(val callback: (Int) -> Unit) : ListAdapter<PhotoViewerModel, PhotoAdjustAdapter.ViewHolder>(getDiffUtilCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemImageTextBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bindViews(getItem(position))


    inner class ViewHolder(val binding: ItemImageTextBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindViews(item: PhotoViewerModel) {
            binding.apply {

                Glide.with(root.context).load(item.drawable).into(image)
                name.text = root.context.getString(item.name)

                if (selectedPosition == layoutPosition) {
                    name.setColor(R.color.primaryColor)
                    image.setSvgColor(R.color.primaryColor)
                } else {
                    name.setColor(R.color.tagline_color)
                    image.setSvgColor(R.color.tagline_color)
                }

                root.setOnClickListener {
                    val copyOfLastCheckedPosition: Int = selectedPosition
                    selectedPosition = layoutPosition
                    notifyItemChanged(copyOfLastCheckedPosition)
                    notifyItemChanged(selectedPosition)
                    callback.invoke(layoutPosition)
                }
            }
        }
    }

    companion object{
        var selectedPosition: Int = 0
    }
}