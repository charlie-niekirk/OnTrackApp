package me.cniekirk.ontrackapp.di

import dev.zacsweers.metro.createGraph

internal val iosGraph by lazy { createGraph<IosOnTrackAppGraph>() }
