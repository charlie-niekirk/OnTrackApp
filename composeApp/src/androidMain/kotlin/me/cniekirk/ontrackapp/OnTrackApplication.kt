package me.cniekirk.ontrackapp

import android.app.Application
import dev.zacsweers.metro.createGraph
import dev.zacsweers.metro.createGraphFactory
import me.cniekirk.ontrackapp.di.AndroidOnTrackAppGraph

class OnTrackApplication : Application() {

    val appGraph by lazy {
        createGraphFactory<AndroidOnTrackAppGraph.Factory>().create(this)
    }

    override fun onCreate() {
        super.onCreate()
    }
}