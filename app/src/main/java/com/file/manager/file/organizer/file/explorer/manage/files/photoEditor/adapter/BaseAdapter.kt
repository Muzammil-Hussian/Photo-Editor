package com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.adapter

import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.R
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.extensions.beVisibleIf

abstract class BaseAdapter<T, VH : RecyclerView.ViewHolder>(diffCallback: DiffUtil.ItemCallback<T>) : ListAdapter<T, VH>(diffCallback) {

    protected var selectedPosition = -1

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        onBindItem(holder, item, position)

        (holder.itemView.findViewById<ImageView>(R.id.selection)).beVisibleIf(position == selectedPosition)

        holder.itemView.setOnClickListener {
            val copyOfLastCheckedPosition: Int = selectedPosition
            selectedPosition = position
            notifyItemChanged(copyOfLastCheckedPosition)
            notifyItemChanged(selectedPosition)
            onItemClick(holder, item, position)
        }
    }

    abstract fun onBindItem(holder: VH, item: T, position: Int)

    abstract fun onItemClick(holder: VH, item: T, position: Int)
}
