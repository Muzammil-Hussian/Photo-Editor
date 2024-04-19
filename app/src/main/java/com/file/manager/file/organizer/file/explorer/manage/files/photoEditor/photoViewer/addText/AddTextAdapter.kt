package com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.photoViewer.addText

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.adapter.BaseAdapter
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.databinding.ColorPickerItemListBinding
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.helper.getDiffUtilCallBack

class AddTextAdapter(private val callback: (Int, ColorModel) -> Unit) : BaseAdapter<ColorModel, AddTextAdapter.ViewHolder>(getDiffUtilCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ColorPickerItemListBinding.inflate( LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindItem(holder: ViewHolder, item: ColorModel, position: Int) {
        holder.binding.apply {
            colorPicker.setBackgroundColor(ContextCompat.getColor(root.context, item.color))
        }
    }

    override fun onItemClick(holder: ViewHolder, item: ColorModel, position: Int) = callback.invoke(position, item)


    inner class ViewHolder(val binding: ColorPickerItemListBinding) : RecyclerView.ViewHolder(binding.root)
}


data class ColorModel(@ColorRes val color: Int)