package com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.photoViewer.editor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.databinding.ItemPhotoEditorBinding
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.helper.getDiffUtilCallBack
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.photoViewer.model.PhotoViewerModel


class PhotoEditorAdapter(val callback: (Int) -> Unit) : ListAdapter<PhotoViewerModel, PhotoEditorAdapter.ViewHolder>(getDiffUtilCallBack<PhotoViewerModel>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPhotoEditorBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bindViews(position, getItem(position))

    inner class ViewHolder(val binding: ItemPhotoEditorBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindViews(position: Int, item: PhotoViewerModel) {
            binding.apply {
                Glide.with(root.context).load(item.drawable).into(image)
                name.text = root.context.getString(item.name)

                parent.setOnClickListener { callback.invoke(position) }
            }
        }
    }
}
