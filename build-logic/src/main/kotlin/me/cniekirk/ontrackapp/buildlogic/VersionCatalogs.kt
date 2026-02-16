package me.cniekirk.ontrackapp.buildlogic

import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.getByType

internal fun Project.libsCatalog(): VersionCatalog =
    extensions.getByType<VersionCatalogsExtension>().named("libs")

internal fun VersionCatalog.requiredVersion(name: String): String =
    findVersion(name)
        .orElseThrow { IllegalArgumentException("Missing version '$name' in libs version catalog") }
        .requiredVersion

internal fun VersionCatalog.requiredLibrary(name: String): Provider<MinimalExternalModuleDependency> =
    findLibrary(name)
        .orElseThrow { IllegalArgumentException("Missing library '$name' in libs version catalog") }
