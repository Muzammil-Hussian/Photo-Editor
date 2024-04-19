package com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.adapter


import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.databinding.ItemGridSelectionBinding
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.databinding.ItemHeaderSelectionBinding
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.extensions.beGone
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.extensions.beVisible
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.extensions.formatDate
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.extensions.isVideoFast
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.model.DateGroupedItems
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.model.ListItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "PhotoFolderAdapter"
const val folderDataType = 1
private const val photosDataType = 3

class PhotosAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var folderList: List<DateGroupedItems> = ArrayList()
    var selectedListNew = ArrayList<ListItem>()
    private val itemList = ArrayList<BaseView>()

    var callBackData: ((Int, ListItem) -> Unit)? = null
    var selectedListCallback: ((Int, newSelectedList: ArrayList<ListItem>, Int, ListItem) -> Unit)? = null
    var invokeCallBackFolderSelection: ((String) -> Unit)? = null
    var folderSelectionCallback: ((String, Boolean, List<ListItem>) -> Unit)? = null

    private var showCheckboxesAdapter = false

    private var mAllowPickingMultiple = false
    private var mIsThirdPartyIntent = false

    @SuppressLint("NotifyDataSetChanged")
      fun setData(list: List<DateGroupedItems>, allowPickingMultiple: Boolean = false, isThirdPartyIntent: Boolean = false) {
        mIsThirdPartyIntent = isThirdPartyIntent
        mAllowPickingMultiple = allowPickingMultiple

        itemList.clear()
        //  selectedListNew.clear()
        folderList = list
        // _ in loop is index
        list.forEachIndexed { _, photoFolderModel ->
            itemList.add(FolderView(photoFolderModel.date, photoFolderModel.isSelected, photoFolderModel.listItems))
            photoFolderModel.listItems.sortedByDescending { it.modified }
            photoFolderModel.listItems.forEachIndexed { _, photoModel ->
                photoModel.isChecked = false
                itemList.add(InnerPhotoView(photoModel))
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when (viewType) {
        folderDataType -> FolderDataHolder(ItemHeaderSelectionBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        else -> PhotoInnerHolder(ItemGridSelectionBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = itemList[position].setViewHolder(position, holder)

    override fun getItemCount(): Int = itemList.size

    override fun getItemViewType(position: Int): Int = itemList[position].getViewType()

    inner class FolderDataHolder(val binding: ItemHeaderSelectionBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NotifyDataSetChanged")
        fun getFolderData(position: Int, date: String, list: List<ListItem>) {
            binding.apply {

                /*       if (showCheckboxesAdapter) {
                          checkboxMainFolder.beVisible()
                      } else {
                          checkboxMainFolder.beInvisible()
                      }

                       if (list.size > 0) {
                            mainLayout.beVisible()
                        } else {
                            mainLayout.beGone()
                        }*/
                this.date.text = date
                checkboxMainFolder.isChecked = (itemList[layoutPosition] as FolderView).isSelected
                //multi select whole folder
                checkboxMainFolder.setOnClickListener {
                    if ((itemList[position] as FolderView).isSelected) {
                        (itemList[position] as FolderView).isSelected = false
                        folderSelectionCallback?.invoke((itemList[position] as FolderView).date, false, list)
                    } else {
                        (itemList[position] as FolderView).isSelected = true
                        folderSelectionCallback?.invoke((itemList[position] as FolderView).date, true, list)
                    }
                }

                //for one item click select or unselect whole folder logic
                invokeCallBackFolderSelection = { date ->
                    for (i in folderList) {
                        if (i.date == date) {
                            if (i.isSelected) {
                                for (item in itemList) {
                                    if (item.getViewType() == folderDataType) {
                                        if ((item as FolderView).date == date) {
                                            item.isSelected = true
                                            break
                                        }
                                    }
                                }
                            } else {
                                for (item in itemList) {
                                    if (item.getViewType() == folderDataType) {
                                        if ((item as FolderView).date == date) {
                                            item.isSelected = false
                                            break
                                        }
                                    }
                                }
                            }
                        }
                    }
                    notifyDataSetChanged()
                }
            }
        }
    }

    inner class PhotoInnerHolder(val binding: ItemGridSelectionBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NotifyDataSetChanged")
        fun bindInnerImagesData(position: Int, model: ListItem) {
            binding.apply {


                // Set the scale based on the selected state
//                itemView.scaleX = if (selectedListNew.contains(model)) 0.8f else 1.0f
//                itemView.scaleY = if (selectedListNew.contains(model)) 0.8f else 1.0f


                if (showCheckboxesAdapter) {
                    checkboxInnerFilesFolder.beVisible()
                } else {
                    checkboxInnerFilesFolder.beGone()
                }
                if (model.path.isVideoFast()) {
                    player.beVisible()
                } else {
                    player.beGone()
                }/*  val width = ivInnerPhotos.layoutParams.width
                  var height = 0
                  when (mHeight) {
                      1 -> height =
                          root.context.resources.getDimensionPixelSize(R.dimen.image_height_1)

                      2 -> height =
                          root.context.resources.getDimensionPixelSize(R.dimen.image_height_2)

                      3 -> height =
                          root.context.resources.getDimensionPixelSize(R.dimen.image_height_3)

                      4 -> height =
                          root.context.resources.getDimensionPixelSize(R.dimen.image_height_4)

                      5 -> height =
                          root.context.resources.getDimensionPixelSize(R.dimen.image_height_5)
                  }
                  val layoutParams = LinearLayout.LayoutParams(width, height)
                  ivInnerPhotos.layoutParams = layoutParams*/

                val options = RequestOptions().signature(ObjectKey(model.path)).skipMemoryCache(false).priority(Priority.LOW).centerCrop().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                Glide.with(itemView.context).load(model.path).apply(options).transition(DrawableTransitionOptions.withCrossFade()).into(itemIcon)

                folderSelectionCallback = { date, check, _ ->
                    for (i in itemList) {
                        if (i.getViewType() != folderDataType) {
                            if (check) {
                                if (date == formatDate(root.context, (i as InnerPhotoView).model.modified)) {
                                    i.model.isChecked = true
                                    selectedListNew.add(i.model)
                                }
                            } else {
                                if (date == formatDate(root.context, (i as InnerPhotoView).model.modified)) {
                                    i.model.isChecked = false
                                    selectedListNew.remove(i.model)
                                }
                            }
                        }
                    }
                    selectedListNew = if (selectedListNew.size > 0) {
                        ArrayList(selectedListNew.distinct())
                    } else {
                        selectedListNew
                    }

                    selectedListCallback?.invoke(selectedListNew.size, selectedListNew, position, model)
                    notifyDataSetChanged()
                }
                checkboxInnerFilesFolder.isChecked = (itemList[position] as InnerPhotoView).model.isChecked

                //each item checkbox handling
                itemView.setOnClickListener {
                    if (!showCheckboxesAdapter) {
                        if (mIsThirdPartyIntent && !mAllowPickingMultiple) {
                            model.isChecked = true
                            selectedListNew.add(model)
                        }
                        callBackData?.invoke(position, model)
                    } else {

//                        toggleImageScale(it,adapterPosition,model)


                        if ((itemList[position] as InnerPhotoView).model.isChecked) {
                            (itemList[position] as InnerPhotoView).model.isChecked = false
                            checkboxInnerFilesFolder.isChecked = false

                            selectedListNew.remove(model)
                            //unselect model from all folder list
                            CoroutineScope(Dispatchers.IO).launch {
                                selectSingleModel(
                                    (itemList[position] as InnerPhotoView).model, false
                                )
                            }.invokeOnCompletion {
                                try {
                                    folderTrueFalse(formatDate(root.context, (itemList[position] as InnerPhotoView).model.modified)) {
                                        invokeCallBackFolderSelection?.invoke(formatDate(root.context, (itemList[position] as InnerPhotoView).model.modified))
                                    }
                                } catch (_: ArrayIndexOutOfBoundsException) {
                                }
                            }
                        } else {
                            selectedListNew.add(model)
                            checkboxInnerFilesFolder.isChecked = true
                            (itemList[position] as InnerPhotoView).model.isChecked = true
                            //select model from all folder list
//                            toggleImageScale(it,binding)


                            CoroutineScope(Dispatchers.IO).launch {
                                selectSingleModel(
                                    (itemList[position] as InnerPhotoView).model, true
                                )
                            }.invokeOnCompletion {
                                try {
                                    folderTrueFalse(formatDate(root.context, (itemList[position] as InnerPhotoView).model.modified)) {
                                        Log.d(TAG, "iterators: $it ")
                                        Log.d(TAG, "diff: ${folderList.size} ")
                                        invokeCallBackFolderSelection?.invoke(formatDate(root.context, (itemList[position] as InnerPhotoView).model.modified))
                                    }
                                } catch (_: ArrayIndexOutOfBoundsException) {
                                }
                            }
                        }
                        //callback item count
                        selectedListCallback?.invoke(selectedListNew.size, selectedListNew, position, model)
                    }
                }



                itemView.setOnLongClickListener {
                    if (mIsThirdPartyIntent && !mAllowPickingMultiple) {
                        model.isChecked = true
                        selectedListNew.add(model)
                        callBackData?.invoke(position, model)
                    } else {
                        setShowCheckboxes(true)
                        model.isChecked = true
                        selectedListNew.add(model)

                        selectedListCallback?.invoke(selectedListNew.size, selectedListNew, position, model)
                    }
                    true
                }
            }
        }
    }

    inner class FolderView(var date: String, var isSelected: Boolean, private var innerList: List<ListItem>) : BaseView() {
        override fun getViewType(): Int {
            return folderDataType
        }

        override fun setViewHolder(position: Int, holder: RecyclerView.ViewHolder) {
            (holder as FolderDataHolder).getFolderData(position, date, innerList)
        }
    }

    inner class InnerPhotoView(val model: ListItem) : BaseView() {
        override fun getViewType(): Int {
            return photosDataType
        }

        override fun setViewHolder(position: Int, holder: RecyclerView.ViewHolder) {
            (holder as PhotoInnerHolder).bindInnerImagesData(position, model)
        }
    }

    abstract inner class BaseView {
        abstract fun getViewType(): Int
        abstract fun setViewHolder(position: Int, holder: RecyclerView.ViewHolder)
    }

    fun selectSingleModel(model: ListItem, check: Boolean) {
        try {
            folderList.forEachIndexed { _, photoFolderModel ->
                photoFolderModel.listItems.forEachIndexed { _, photoModel ->
                    if (model.path == photoModel.path) {
                        photoModel.isChecked = check
                    }
                }
            }
        } catch (_: Exception) {
        }
    }

    fun folderTrueFalse(date: String, res: (Boolean) -> Unit = {}) {
        try {
            var selected = false
            CoroutineScope(Dispatchers.IO).launch {
                folderList.forEachIndexed { _, photoFolderModel ->
                    if (date == photoFolderModel.date) {
                        for (i in photoFolderModel.listItems) {
                            if (i.isChecked) {
                                photoFolderModel.isSelected = true
                                selected = true
                                Log.d("TAG", "folderTrueFalse: ")
                            } else {
                                photoFolderModel.isSelected = false
                                selected = false
                                break
                            }
                        }
                    }
                }
            }.invokeOnCompletion {
                CoroutineScope(Dispatchers.Main).launch {
                    res.invoke(selected)
                }
            }
        } catch (_: Exception) {
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setShowCheckboxes(showCheckboxes: Boolean) {
        showCheckboxesAdapter = showCheckboxes
        notifyDataSetChanged()
    }
}








