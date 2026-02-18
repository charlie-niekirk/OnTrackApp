package me.cniekirk.ontrackapp.core.datastore.di

import androidx.datastore.core.DataStore
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import me.cniekirk.ontrackapp.core.datastore.PinnedServicesDataSource
import me.cniekirk.ontrackapp.core.datastore.PinnedServicesDataSourceImpl
import me.cniekirk.ontrackapp.core.datastore.RecentSearchesDataSource
import me.cniekirk.ontrackapp.core.datastore.RecentSearchesDataSourceImpl
import me.cniekirk.ontrackapp.core.datastore.ThemePreferencesDataSource
import me.cniekirk.ontrackapp.core.datastore.ThemePreferencesDataSourceImpl
import me.cniekirk.ontrackapp.core.datastore.model.PinnedServices
import me.cniekirk.ontrackapp.core.datastore.model.RecentSearches
import me.cniekirk.ontrackapp.core.datastore.model.ThemePreferences

@BindingContainer
object CommonDatastoreProviders {

    @Provides
    @SingleIn(AppScope::class)
    fun providePinnedServicesDataSource(
        dataStore: DataStore<PinnedServices>
    ): PinnedServicesDataSource {
        return PinnedServicesDataSourceImpl(dataStore)
    }

    @Provides
    @SingleIn(AppScope::class)
    fun provideRecentSearchesDataSource(
        dataStore: DataStore<RecentSearches>
    ): RecentSearchesDataSource {
        return RecentSearchesDataSourceImpl(dataStore)
    }

    @Provides
    @SingleIn(AppScope::class)
    fun provideThemePreferencesDataSource(
        dataStore: DataStore<ThemePreferences>
    ): ThemePreferencesDataSource {
        return ThemePreferencesDataSourceImpl(dataStore)
    }
}
