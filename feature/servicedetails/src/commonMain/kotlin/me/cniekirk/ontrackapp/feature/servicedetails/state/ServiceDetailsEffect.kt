package me.cniekirk.ontrackapp.feature.servicedetails.state

sealed interface ServiceDetailsEffect {
    data object DisplayError : ServiceDetailsEffect
}
