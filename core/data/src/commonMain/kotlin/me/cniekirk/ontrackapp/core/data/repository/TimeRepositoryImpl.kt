package me.cniekirk.ontrackapp.core.data.repository

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import me.cniekirk.ontrackapp.core.data.time.TimeUtils
import me.cniekirk.ontrackapp.core.domain.model.RunDate
import me.cniekirk.ontrackapp.core.domain.repository.TimeRepository

@ContributesBinding(AppScope::class)
@Inject
class TimeRepositoryImpl : TimeRepository {

    override fun currentDateMillis(): Long
        = TimeUtils.getCurrentEpochMillis()

    override fun convertMillisToDateString(millis: Long): String =
        TimeUtils.formatMillisToDateString(millis)

    override fun parseRunDate(dateString: String): RunDate {
        return RunDate("", "", "")
    }
}