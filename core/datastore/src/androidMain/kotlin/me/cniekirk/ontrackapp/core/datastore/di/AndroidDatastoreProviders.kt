package me.cniekirk.ontrackapp.core.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.okio.OkioStorage
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import me.cniekirk.ontrackapp.core.common.di.ApplicationContext
import me.cniekirk.ontrackapp.core.datastore.PINNED_SERVICES_FILE_NAME
import me.cniekirk.ontrackapp.core.datastore.RECENT_SEARCHES_FILE_NAME
import me.cniekirk.ontrackapp.core.datastore.THEME_PREFERENCES_FILE_NAME
import me.cniekirk.ontrackapp.core.datastore.getDataStorePathWithContext
import me.cniekirk.ontrackapp.core.datastore.model.PinnedServices
import me.cniekirk.ontrackapp.core.datastore.model.RecentSearches
import me.cniekirk.ontrackapp.core.datastore.model.ThemePreferences
import me.cniekirk.ontrackapp.core.datastore.serializer.PinnedServicesSerializer
import me.cniekirk.ontrackapp.core.datastore.serializer.RecentSearchesSerializer
import me.cniekirk.ontrackapp.core.datastore.serializer.ThemePreferencesSerializer
import okio.FileSystem

@BindingContainer(includes = [CommonDatastoreProviders::class])
object AndroidDatastoreProviders {

    @Provides
    @SingleIn(AppScope::class)
    fun providePinnedServicesDataStore(
        @ApplicationContext context: Context
    ): DataStore<PinnedServices> {
        return DataStoreFactory.create(
            storage = OkioStorage(
                fileSystem = FileSystem.SYSTEM,
                serializer = PinnedServicesSerializer,
                producePath = { getDataStorePathWithContext(context, PINNED_SERVICES_FILE_NAME) }
            )
        )
    }

    @Provides
    @SingleIn(AppScope::class)
    fun provideRecentSearchesDataStore(
        @ApplicationContext context: Context
    ): DataStore<RecentSearches> {
        return DataStoreFactory.create(
            storage = OkioStorage(
                fileSystem = FileSystem.SYSTEM,
                serializer = RecentSearchesSerializer,
                producePath = { getDataStorePathWithContext(context, RECENT_SEARCHES_FILE_NAME) }
            )
        )
    }

    @Provides
    @SingleIn(AppScope::class)
    fun provideThemePreferencesDataStore(
        @ApplicationContext context: Context
    ): DataStore<ThemePreferences> {
        return DataStoreFactory.create(
            storage = OkioStorage(
                fileSystem = FileSystem.SYSTEM,
                serializer = ThemePreferencesSerializer,
                producePath = { getDataStorePathWithContext(context, THEME_PREFERENCES_FILE_NAME) }
            )
        )
    }
}
