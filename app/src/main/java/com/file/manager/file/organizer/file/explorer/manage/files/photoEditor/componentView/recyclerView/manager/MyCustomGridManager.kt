package com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.componentView.recyclerView.manager

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager

class MyCustomGridManager : GridLayoutManager {
    constructor(context: Context, spanCount: Int) : super(context, spanCount)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
            context,
            attrs,
            defStyleAttr,
            defStyleRes
    )

    constructor(context: Context, spanCount: Int, orientation: Int, reverseLayout: Boolean) : super(
            context,
            spanCount,
            orientation,
            reverseLayout
    )

    override fun supportsPredictiveItemAnimations() = false
}
