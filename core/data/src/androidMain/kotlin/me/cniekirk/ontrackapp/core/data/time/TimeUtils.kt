package me.cniekirk.ontrackapp.core.data.time

import java.text.SimpleDateFormat
import java.util.Locale

actual object TimeUtils {

    actual fun getCurrentEpochMillis(): Long =
        System.currentTimeMillis()

    actual fun formatMillisToDateString(millis: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(millis)
    }

}