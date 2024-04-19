package com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.extensions

import android.content.Context
import android.text.format.DateUtils
import java.util.Calendar


val calendar: Calendar = Calendar.getInstance()
fun formatDate(context: Context, timestamp: Long): String {

    val today = Calendar.getInstance()
    val yesterday = Calendar.getInstance()
    yesterday.add(Calendar.DAY_OF_YEAR, -1)

    calendar.timeInMillis = timestamp

    return when {
        DateUtils.isToday(timestamp) -> "Today"
        calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == yesterday.get(
            Calendar.DAY_OF_YEAR
        ) -> "Yesterday"

        else -> DateUtils.formatDateTime(context, timestamp, DateUtils.FORMAT_SHOW_DATE)
    }
}