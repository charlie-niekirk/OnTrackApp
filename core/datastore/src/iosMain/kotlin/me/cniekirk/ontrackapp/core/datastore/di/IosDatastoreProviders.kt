package me.cniekirk.ontrackapp.core.datastore.di

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.okio.OkioStorage
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import me.cniekirk.ontrackapp.core.datastore.RECENT_SEARCHES_FILE_NAME
import me.cniekirk.ontrackapp.core.datastore.getDataStorePath
import me.cniekirk.ontrackapp.core.datastore.model.RecentSearches
import me.cniekirk.ontrackapp.core.datastore.serializer.RecentSearchesSerializer
import okio.FileSystem

@BindingContainer(includes = [CommonDatastoreProviders::class])
object IosDatastoreProviders {

    @Provides
    @SingleIn(AppScope::class)
    fun provideRecentSearchesDataStore(): DataStore<RecentSearches> {
        return DataStoreFactory.create(
            storage = OkioStorage(
                fileSystem = FileSystem.SYSTEM,
                serializer = RecentSearchesSerializer,
                producePath = { getDataStorePath(RECENT_SEARCHES_FILE_NAME) }
            )
        )
    }
}
