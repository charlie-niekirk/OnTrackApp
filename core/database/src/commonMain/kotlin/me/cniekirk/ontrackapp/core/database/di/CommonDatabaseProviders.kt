package me.cniekirk.ontrackapp.core.database.di

import app.cash.sqldelight.db.SqlDriver
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import me.cniekirk.ontrackapp.core.database.StationDatabase

@BindingContainer
object CommonDatabaseProviders {

    @Provides
    @SingleIn(AppScope::class)
    fun provideStationDatabase(sqlDriver: SqlDriver): StationDatabase {
        return StationDatabase(sqlDriver)
    }
}