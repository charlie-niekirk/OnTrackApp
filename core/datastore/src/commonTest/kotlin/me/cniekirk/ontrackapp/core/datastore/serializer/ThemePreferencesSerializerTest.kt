package me.cniekirk.ontrackapp.core.datastore.serializer

import kotlinx.coroutines.test.runTest
import me.cniekirk.ontrackapp.core.domain.model.theme.ThemeMode
import okio.Buffer
import kotlin.test.Test
import kotlin.test.assertEquals

class ThemePreferencesSerializerTest {

    @Test
    fun malformedJsonFallsBackToSystemTheme() = runTest {
        val malformedJson = "{ this is not valid json }"

        val result = ThemePreferencesSerializer.readFrom(Buffer().writeUtf8(malformedJson))

        assertEquals(ThemeMode.SYSTEM, result.themeMode)
    }
}
