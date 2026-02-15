package me.cniekirk.ontrackapp.core.data.repository

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import me.cniekirk.ontrackapp.core.domain.model.RunDate
import me.cniekirk.ontrackapp.core.domain.model.servicedetails.ServiceDetails
import me.cniekirk.ontrackapp.core.domain.model.services.Platform
import me.cniekirk.ontrackapp.core.domain.model.services.ServiceLocation
import me.cniekirk.ontrackapp.core.domain.model.services.TimeStatus
import me.cniekirk.ontrackapp.core.domain.model.services.TrainService
import me.cniekirk.ontrackapp.core.domain.repository.RealtimeTrainsRepository
import me.cniekirk.ontrackapp.core.network.model.realtimetrains.common.ServiceLocationType
import me.cniekirk.ontrackapp.core.network.model.realtimetrains.common.ServiceStopLocation
import me.cniekirk.ontrackapp.core.network.model.realtimetrains.servicelist.BoardService
import me.cniekirk.ontrackapp.core.network.service.RealTimeTrainsService
import me.cniekirk.ontrackapp.core.domain.model.servicedetails.Location as DomainLocation

@ContributesBinding(AppScope::class)
@Inject
class RealTimeTrainsRepositoryImpl(
    private val realTimeTrainsService: RealTimeTrainsService
) : RealtimeTrainsRepository {

    override suspend fun getDepartureBoardOnDateTime(
        station: String,
        year: String,
        month: String,
        day: String,
        time: String
    ): Result<List<TrainService>> {
        return runCatching {
            val response = realTimeTrainsService.getDeparturesOnDateTime(
                location = station,
                year = year,
                month = month,
                day = day,
                time = time
            )
            response.services.map { it.toDomainModel() }
        }
    }

    override suspend fun getDepartureBoardOnDateTimeTo(
        fromStation: String,
        toStation: String,
        year: String,
        month: String,
        day: String,
        time: String
    ): Result<List<TrainService>> {
        return runCatching {
            val response = realTimeTrainsService.getDeparturesToOnDateTime(
                location = fromStation,
                toLocation = toStation,
                year = year,
                month = month,
                day = day,
                time = time
            )
            response.services.map { it.toDomainModel() }
        }
    }

    override suspend fun getArrivalBoardOnDateTime(
        station: String,
        year: String,
        month: String,
        day: String,
        time: String
    ): Result<List<TrainService>> {
        return runCatching {
            val response = realTimeTrainsService.getArrivalsOnDateTime(
                location = station,
                year = year,
                month = month,
                day = day,
                time = time
            )
            response.services.map { it.toDomainModel() }
        }
    }

    override suspend fun getArrivalBoardOnDateTimeFrom(
        atStation: String,
        fromStation: String,
        year: String,
        month: String,
        day: String,
        time: String
    ): Result<List<TrainService>> {
        return runCatching {
            val response = realTimeTrainsService.getArrivalsFromOnDateTime(
                location = atStation,
                fromLocation = fromStation,
                year = year,
                month = month,
                day = day,
                time = time
            )
            response.services.map { it.toDomainModel() }
        }
    }

    override suspend fun getCurrentDepartureBoard(station: String): Result<List<TrainService>> {
        return runCatching {
            val response = realTimeTrainsService.getCurrentDepartures(location = station)
            response.services.map { it.toDomainModel() }
        }
    }

    override suspend fun getCurrentDepartureBoardTo(
        fromStation: String,
        toStation: String
    ): Result<List<TrainService>> {
        return runCatching {
            val response = realTimeTrainsService.getCurrentDeparturesTo(
                location = fromStation,
                toLocation = toStation
            )
            response.services.map { it.toDomainModel() }
        }
    }

    override suspend fun getCurrentArrivalBoard(station: String): Result<List<TrainService>> {
        return runCatching {
            val response = realTimeTrainsService.getCurrentArrivals(location = station)
            response.services.map { it.toDomainModel() }
        }
    }

    override suspend fun getCurrentArrivalBoardFrom(
        atStation: String,
        fromStation: String
    ): Result<List<TrainService>> {
        return runCatching {
            val response = realTimeTrainsService.getCurrentArrivalsFrom(
                location = atStation,
                fromLocation = fromStation
            )
            response.services.map { it.toDomainModel() }
        }
    }

    override suspend fun getServiceDetails(
        serviceUid: String,
        year: String,
        month: String,
        day: String
    ): Result<ServiceDetails> {
        return runCatching {
            val response = realTimeTrainsService.getServiceDetails(
                serviceUid = serviceUid,
                year = year,
                month = month,
                day = day
            )
            response.toDomainModel()
        }
    }

    // Mapper extension functions
    private fun BoardService.toDomainModel(): TrainService {
        val runDateParts = runDate.split("-")
        return TrainService(
            serviceId = serviceUid,
            runDate = RunDate(
                day = runDateParts.getOrNull(2) ?: "",
                month = runDateParts.getOrNull(1) ?: "",
                year = runDateParts.getOrNull(0) ?: ""
            ),
            origin = locationDetail.origin.firstOrNull()?.description ?: "",
            destination = locationDetail.destination.firstOrNull()?.description ?: "",
            timeStatus = locationDetail.mapToTimeStatus(isDeparture = true),
            platform = locationDetail.mapToPlatform(),
            serviceLocation = locationDetail.serviceLocation?.toDomainModel(),
            trainOperatingCompany = atocName
        )
    }

    private fun me.cniekirk.ontrackapp.core.network.model.realtimetrains.servicedetail.ServiceDetail.toDomainModel(): ServiceDetails {
        return ServiceDetails(
            trainOperatingCompany = atocName,
            origin = origin.firstOrNull()?.description ?: "",
            destination = destination.firstOrNull()?.description ?: "",
            locations = locations.map { it.toDomainModel() }
        )
    }

    private fun ServiceStopLocation.toDomainModel(): DomainLocation {
        return DomainLocation(
            locationName = description,
            departureTimeStatus = mapToTimeStatus(isDeparture = true),
            arrivalTimeStatus = mapToTimeStatus(isDeparture = false),
            platform = mapToPlatform() ?: Platform.Confirmed(
                platformName = "TBC",
                isChanged = false
            ),
            serviceLocation = serviceLocation?.toDomainModel(),
            crs = crs
        )
    }

    private fun ServiceStopLocation.mapToTimeStatus(isDeparture: Boolean): TimeStatus {
        if (isCancelled) {
            val scheduledTime = if (isDeparture) gbttBookedDeparture else gbttBookedArrival
            return TimeStatus.Cancelled(
                scheduledDepartureTime = scheduledTime ?: "",
                reason = cancelReasonLongText ?: cancelReasonShortText ?: "Unknown reason"
            )
        }

        val scheduledTime = if (isDeparture) gbttBookedDeparture else gbttBookedArrival
        val realtimeTime = if (isDeparture) realtimeDeparture else realtimeArrival
        val actualTime = if (isDeparture) realtimeDepartureActual else realtimeArrivalActual
        val lateness = if (isDeparture) realtimeGbttDepartureLateness else realtimeGbttArrivalLateness

        return when {
            scheduledTime == null -> TimeStatus.Unknown
            actualTime && realtimeTime != null -> {
                if (isDeparture) {
                    TimeStatus.Departed(
                        actualDepartureTime = realtimeTime,
                        scheduledDepartureTime = scheduledTime,
                        delayInMinutes = lateness ?: 0
                    )
                } else {
                    TimeStatus.Arrived(
                        actualArrivalTime = realtimeTime,
                        scheduledArrivalTime = scheduledTime,
                        delayInMinutes = lateness ?: 0
                    )
                }
            }
            realtimeTime != null && (lateness ?: 0) > 0 -> {
                TimeStatus.Delayed(
                    scheduledTime = scheduledTime,
                    estimatedTime = realtimeTime,
                    delayInMinutes = lateness ?: 0
                )
            }
            else -> TimeStatus.OnTime(scheduledTime = scheduledTime)
        }
    }

    private fun ServiceStopLocation.mapToPlatform(): Platform? {
        val platformValue = platform
        if (platformValue == null || platformValue == "none") return null

        return if (platformConfirmed) {
            Platform.Confirmed(
                platformName = platformValue,
                isChanged = platformChanged
            )
        } else {
            Platform.Estimated(
                platformName = platformValue,
                isChanged = platformChanged
            )
        }
    }

    private fun ServiceLocationType.toDomainModel(): ServiceLocation {
        return when (this) {
            ServiceLocationType.APPR_STAT -> ServiceLocation.APPROACHING_STATION
            ServiceLocationType.APPR_PLAT -> ServiceLocation.APPROACHING_PLATFORM
            ServiceLocationType.AT_PLAT -> ServiceLocation.AT_PLATFORM
            ServiceLocationType.DEP_PREP -> ServiceLocation.PREPARING_DEPARTURE
            ServiceLocationType.DEP_READY -> ServiceLocation.READY_TO_DEPART
        }
    }
}
