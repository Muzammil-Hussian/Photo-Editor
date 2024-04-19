package com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.photoViewer.background

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.databinding.ItemBackgroundFragmentBinding
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.extensions.beVisibleIf
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.helper.getDiffUtilCallBack

class BackgroundAdapter(private val callback: (Int, BackgroundItemsModel) -> Unit) : ListAdapter<BackgroundItemsModel, BackgroundAdapter.ViewHolder>(getDiffUtilCallBack()) {

    private var selectedPositionsByType = hashMapOf<String, Int>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemBackgroundFragmentBinding.inflate(layoutInflater)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position, getItem(position))
    }


    inner class ViewHolder(private val binding: ItemBackgroundFragmentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int, item: BackgroundItemsModel) {

            val typeIdentifier = item.getTypeIdentifier()
            val isSelected = selectedPositionsByType[typeIdentifier] == position

            binding.apply {

                selection.beVisibleIf(isSelected)

                root.setOnClickListener {
                    val previousSelectedPosition = selectedPositionsByType[typeIdentifier] ?: -1
                    selectedPositionsByType[typeIdentifier] = position
                    if (previousSelectedPosition >= 0) { notifyItemChanged(previousSelectedPosition) }
                    notifyItemChanged(position)
                    callback.invoke(position, item)
                }

                background.apply {

                    when (item) {
                        is BackgroundItemsModel.SolidColor -> {
                            val resourceId = ColorDrawable(ContextCompat.getColor(context, item.color))
                            Glide.with(context).load(resourceId).into(this)
                        }

                        is BackgroundItemsModel.GradientColor -> {
                            val colorsInt = item.colors.map { colorResId -> ContextCompat.getColor(context, colorResId) }.toIntArray()
                            val gradientDrawable = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colorsInt)
                            Glide.with(context).load(gradientDrawable).into(this)
                        }

                        is BackgroundItemsModel.Pattern -> Glide.with(context).load(item.resourceId).into(this)
                    }
                }
            }
        }
    }

    fun setSelection(typeIdentifier: String, selection: Int = 0) {
        selectedPositionsByType[typeIdentifier] = selection
    }

}