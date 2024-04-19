package com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.extensions

import android.widget.TextView
import androidx.core.content.ContextCompat


fun TextView.setColor(color: Int) = setTextColor(ContextCompat.getColor(this.context, color))
