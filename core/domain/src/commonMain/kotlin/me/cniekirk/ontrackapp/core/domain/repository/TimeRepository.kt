package me.cniekirk.ontrackapp.core.domain.repository

import me.cniekirk.ontrackapp.core.domain.model.RunDate

interface TimeRepository {

    fun currentDateMillis(): Long

    fun convertMillisToDateString(millis: Long): String

    /**
     * Parses a date string in YYYY-MM-DD format into a LocalDate object.
     *
     * @param dateString The date string in YYYY-MM-DD format (e.g., "2024-03-15")
     * @return A LocalDate object parsed from the date string
     */
    fun parseRunDate(dateString: String): RunDate
}