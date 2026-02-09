package me.cniekirk.ontrackapp.core.datastore

import android.content.Context
import okio.Path
import okio.Path.Companion.toPath

internal actual fun getDataStorePath(fileName: String): Path {
    throw IllegalStateException("Use getDataStorePathWithContext on Android")
}

internal fun getDataStorePathWithContext(context: Context, fileName: String): Path {
    return context.filesDir.resolve(fileName).absolutePath.toPath()
}