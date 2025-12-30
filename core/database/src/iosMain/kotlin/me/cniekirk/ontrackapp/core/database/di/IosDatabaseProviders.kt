package me.cniekirk.ontrackapp.core.database.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import me.cniekirk.ontrackapp.core.database.DatabaseConstants
import me.cniekirk.ontrackapp.core.database.StationDatabase

@BindingContainer(includes = [CommonDatabaseProviders::class])
object IosDatabaseProviders {

    @Provides
    @SingleIn(AppScope::class)
    fun provideDriver(): SqlDriver {
        return NativeSqliteDriver(StationDatabase.Schema, DatabaseConstants.DB_NAME)
    }
}