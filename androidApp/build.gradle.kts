plugins {
    id("ontrack.android.application")
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    id("dev.zacsweers.metro")
}

android {
    namespace = "me.cniekirk.ontrackapp.android"

    defaultConfig {
        applicationId = "me.cniekirk.ontrackapp"
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("debug")
        }
    }
}

dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.org.jetbrains.compose.runtime)
    implementation(libs.org.jetbrains.compose.ui)
    implementation(libs.org.jetbrains.compose.foundation)
    implementation(libs.org.jetbrains.compose.material3)
    implementation(libs.org.jetbrains.compose.material.icons.extended)
    implementation(libs.androidx.datastore.core.okio)

    implementation(libs.dev.zacsweers.metrox.android)
    implementation(libs.dev.zacsweers.metrox.viewmodel)
    implementation(libs.dev.zacsweers.metrox.viewmodel.compose)

    implementation(projects.composeApp)
}
