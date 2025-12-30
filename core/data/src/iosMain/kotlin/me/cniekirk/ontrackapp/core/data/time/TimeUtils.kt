package me.cniekirk.ontrackapp.core.data.time

import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSTimeZone
import platform.Foundation.localTimeZone
import platform.Foundation.timeIntervalSince1970

actual object TimeUtils {

    actual fun getCurrentEpochMillis(): Long {
        return (NSDate().timeIntervalSince1970 * 1000).toLong()
    }

    actual fun formatMillisToDateString(millis: Long): String {
        val formatter = NSDateFormatter().apply {
            dateFormat = "dd/MM/yyyy"
            timeZone = NSTimeZone.localTimeZone
        }
        val date = NSDate(millis.toDouble() / 1000)
        return formatter.stringFromDate(date)
    }
}