package com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.photoViewer.crop

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.photoViewer.model.PhotoViewerModel
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.R
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.databinding.ItemImageTextBinding
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.helper.getDiffUtilCallBack


class CropAdapter(val callback: (item: PhotoViewerModel) -> Unit) : ListAdapter<PhotoViewerModel, CropAdapter.ViewHolder>(getDiffUtilCallBack<PhotoViewerModel>()) {

    private var selectedPosition: Int = 0

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

                if (selectedPosition == layoutPosition) parent.background = ContextCompat.getDrawable(root.context, R.drawable.bg_round_selection_stroke_1px) else root.background = null

                root.setOnClickListener {
                    val copyOfLastCheckedPosition: Int = selectedPosition
                    selectedPosition = layoutPosition
                    notifyItemChanged(copyOfLastCheckedPosition)
                    notifyItemChanged(selectedPosition)

                    callback.invoke(item)
                }
            }
        }
    }
}
