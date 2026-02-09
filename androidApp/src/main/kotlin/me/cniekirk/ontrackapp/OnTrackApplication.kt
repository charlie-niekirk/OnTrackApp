package me.cniekirk.ontrackapp

import android.app.Application
import dev.zacsweers.metro.createGraphFactory
import dev.zacsweers.metrox.android.MetroAppComponentProviders
import dev.zacsweers.metrox.android.MetroApplication
import me.cniekirk.ontrackapp.di.AndroidOnTrackAppGraph

class OnTrackApplication : Application(), MetroApplication {

    override val appComponentProviders: MetroAppComponentProviders by lazy {
        createGraphFactory<AndroidOnTrackAppGraph.Factory>().create(this)
    }
}