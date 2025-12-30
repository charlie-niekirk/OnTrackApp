package me.cniekirk.ontrackapp.core.data.time

expect object TimeUtils {

    fun getCurrentEpochMillis(): Long

    fun formatMillisToDateString(millis: Long): String
}