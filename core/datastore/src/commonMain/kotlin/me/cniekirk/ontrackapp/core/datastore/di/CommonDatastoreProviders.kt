package me.cniekirk.ontrackapp.core.datastore.di

import androidx.datastore.core.DataStore
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import me.cniekirk.ontrackapp.core.datastore.RecentSearchesDataSource
import me.cniekirk.ontrackapp.core.datastore.RecentSearchesDataSourceImpl
import me.cniekirk.ontrackapp.core.datastore.model.RecentSearches

@BindingContainer
object CommonDatastoreProviders {

    @Provides
    @SingleIn(AppScope::class)
    fun provideRecentSearchesDataSource(
        dataStore: DataStore<RecentSearches>
    ): RecentSearchesDataSource {
        return RecentSearchesDataSourceImpl(dataStore)
    }
}
