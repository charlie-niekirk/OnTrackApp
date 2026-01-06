package me.cniekirk.ontrackapp.core.database.di

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import me.cniekirk.ontrackapp.core.common.di.ApplicationContext
import me.cniekirk.ontrackapp.core.database.DatabaseConstants
import me.cniekirk.ontrackapp.core.database.StationDatabase

@BindingContainer(includes = [CommonDatabaseProviders::class])
object AndroidDatabaseProviders {

    @Provides
    @SingleIn(AppScope::class)
    fun provideSqlDriver(@ApplicationContext context: Context): SqlDriver {
        return AndroidSqliteDriver(StationDatabase.Schema, context, DatabaseConstants.DB_NAME)
    }
}