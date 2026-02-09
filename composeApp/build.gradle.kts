import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    id("dev.zacsweers.metro")
}

kotlin {
    androidLibrary {
        namespace = "me.cniekirk.ontrackapp"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }

        // Required while Android resources support for KMP remains experimental.
        experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    sourceSets {
        commonMain.dependencies {
            implementation(libs.org.jetbrains.compose.runtime)
            implementation(libs.org.jetbrains.compose.ui)
            implementation(libs.org.jetbrains.compose.foundation)
            implementation(libs.org.jetbrains.compose.animation)
            implementation(libs.org.jetbrains.compose.material3)
            implementation(libs.org.jetbrains.compose.material.icons.core)
            implementation(libs.org.jetbrains.compose.ui.tooling.preview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation(libs.org.jetbrains.kotlinx.serialization.json)

            implementation(libs.org.jetbrains.navigation3.ui)
            implementation(libs.org.jetbrains.lifecycle.viewmodel.navigation3)

            implementation(libs.dev.zacsweers.metrox.viewmodel)
            implementation(libs.dev.zacsweers.metrox.viewmodel.compose)

            // Expose for :androidApp module
            api(projects.core.common)
            api(projects.core.data)
            api(projects.core.domain)
            api(projects.core.network)
            api(projects.core.database)
            api(projects.core.datastore)
            implementation(projects.core.designsystem)
            api(projects.feature.home)
            api(projects.feature.stationsearch)
            api(projects.feature.servicelist)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.org.jetbrains.compose.ui.tooling)
    androidRuntimeClasspath(libs.org.jetbrains.compose.components.resources)
}
