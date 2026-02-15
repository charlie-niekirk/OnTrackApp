package me.cniekirk.ontrackapp.feature.servicedetails

sealed interface ServiceDetailsEffect {
    data object DisplayError : ServiceDetailsEffect
}
