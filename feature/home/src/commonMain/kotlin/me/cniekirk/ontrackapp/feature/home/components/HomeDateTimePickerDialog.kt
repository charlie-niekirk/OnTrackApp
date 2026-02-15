package me.cniekirk.ontrackapp.feature.home.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import me.cniekirk.ontrackapp.core.domain.model.arguments.RequestTime
import ontrackapp.feature.home.generated.resources.Res
import ontrackapp.feature.home.generated.resources.date_picker_cancel
import ontrackapp.feature.home.generated.resources.date_picker_confirm
import ontrackapp.feature.home.generated.resources.request_time_title
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Clock
import kotlin.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeDateTimePickerDialog(
    visible: Boolean,
    initialRequestTime: RequestTime,
    onDismissRequest: () -> Unit,
    onConfirmRequestTime: (RequestTime.AtTime) -> Unit
) {
    if (!visible) return

    val initialSelection = remember(visible, initialRequestTime) {
        initialRequestTime.toInitialDateTimeSelection()
    }
    var selectedDateMillis by remember(visible, initialSelection.dateMillisUtc) {
        mutableLongStateOf(initialSelection.dateMillisUtc)
    }
    var pickerStep by remember(visible) { mutableStateOf(PickerStep.DATE) }

    when (pickerStep) {
        PickerStep.DATE -> {
            val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedDateMillis)

            DatePickerDialog(
                onDismissRequest = onDismissRequest,
                confirmButton = {
                    TextButton(
                        onClick = {
                            selectedDateMillis = datePickerState.selectedDateMillis ?: selectedDateMillis
                            pickerStep = PickerStep.TIME
                        }
                    ) {
                        Text(text = stringResource(Res.string.date_picker_confirm))
                    }
                },
                dismissButton = {
                    TextButton(onClick = onDismissRequest) {
                        Text(text = stringResource(Res.string.date_picker_cancel))
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        PickerStep.TIME -> {
            val timePickerState = rememberTimePickerState(
                initialHour = initialSelection.hour,
                initialMinute = initialSelection.minute
            )

            AlertDialog(
                onDismissRequest = onDismissRequest,
                title = {
                    Text(text = stringResource(Res.string.request_time_title))
                },
                text = {
                    TimePicker(state = timePickerState)
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val selectedDate = Instant.fromEpochMilliseconds(selectedDateMillis)
                                .toLocalDateTime(TimeZone.UTC)
                                .date

                            onConfirmRequestTime(
                                RequestTime.AtTime(
                                    year = selectedDate.year.toString(),
                                    month = selectedDate.month.number.toString().padStart(2, '0'),
                                    day = selectedDate.day.toString().padStart(2, '0'),
                                    hours = timePickerState.hour.toString().padStart(2, '0'),
                                    mins = timePickerState.minute.toString().padStart(2, '0')
                                )
                            )
                        }
                    ) {
                        Text(text = stringResource(Res.string.date_picker_confirm))
                    }
                },
                dismissButton = {
                    TextButton(onClick = onDismissRequest) {
                        Text(text = stringResource(Res.string.date_picker_cancel))
                    }
                }
            )
        }
    }
}

private enum class PickerStep {
    DATE,
    TIME
}

private data class InitialDateTimeSelection(
    val dateMillisUtc: Long,
    val hour: Int,
    val minute: Int
)

private fun RequestTime.toInitialDateTimeSelection(): InitialDateTimeSelection {
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    if (this is RequestTime.AtTime) {
        val year = year.toIntOrNull()
        val month = month.toIntOrNull()
        val day = day.toIntOrNull()
        val hour = hours.toIntOrNull()
        val minute = mins.toIntOrNull()

        if (
            year != null &&
            month != null &&
            day != null &&
            hour != null &&
            minute != null
        ) {
            runCatching {
                InitialDateTimeSelection(
                    dateMillisUtc = LocalDate(
                        year = year,
                        month = month,
                        day = day
                    ).atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds(),
                    hour = hour.coerceIn(0, 23),
                    minute = minute.coerceIn(0, 59)
                )
            }.getOrNull()?.let { return it }
        }
    }

    return InitialDateTimeSelection(
        dateMillisUtc = now.date.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds(),
        hour = now.hour,
        minute = now.minute
    )
}
