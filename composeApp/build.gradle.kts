import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    id("dev.zacsweers.metro")
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
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
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(libs.org.jetbrains.compose.runtime)
            implementation(libs.org.jetbrains.compose.ui)
            implementation(libs.org.jetbrains.compose.foundation)
            implementation(libs.org.jetbrains.compose.animation)
            implementation(libs.org.jetbrains.compose.material3)
            implementation(libs.org.jetbrains.compose.material.icons.core)
            implementation(libs.org.jetbrains.compose.components.ui.tooling.preview)
            implementation(libs.org.jetbrains.compose.components.resources)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation(libs.org.jetbrains.kotlinx.serialization.json)

            implementation(libs.org.jetbrains.navigation3.ui)
            implementation(libs.org.jetbrains.lifecycle.viewmodel.navigation3)

            implementation(libs.dev.zacsweers.metrox.viewmodel)
            implementation(libs.dev.zacsweers.metrox.viewmodel.compose)

            implementation(projects.core.common)
            implementation(projects.core.data)
            implementation(projects.core.domain)
            implementation(projects.core.network)
            implementation(projects.core.database)
            implementation(projects.core.datastore)
            implementation(projects.feature.home)
            implementation(projects.feature.stationsearch)
            implementation(projects.feature.servicelist)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "me.cniekirk.ontrackapp"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "me.cniekirk.ontrackapp"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

