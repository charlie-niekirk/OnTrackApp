import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    id("dev.zacsweers.metro")
}

android {
    namespace = "me.cniekirk.ontrackapp.android"
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
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
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
