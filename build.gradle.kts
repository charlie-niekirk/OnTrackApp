buildscript {
    dependencies {
        // AGP 9.0 built-in Kotlin defaults to 2.2.10; keep Metro-compatible KGP until it catches up.
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.3.20-Beta2")
    }
}

plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.androidKotlinMultiplatformLibrary) apply false
    alias(libs.plugins.dev.zacsweers.metro) apply false
    alias(libs.plugins.androidLint) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
}

listOf(
    ":core",
    ":feature",
).forEach { path ->
    project(path).pluginManager.apply("base")
}
