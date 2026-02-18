plugins {
    id("ontrack.kmp.base")
    id("ontrack.kmp.compose")
    alias(libs.plugins.kotlinSerialization)
    id("dev.zacsweers.metro")
}

onTrackKmp {
    namespace = "me.cniekirk.ontrackapp"
    frameworkBaseName = "ComposeApp"
    staticFramework = true
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.org.jetbrains.compose.runtime)
            implementation(libs.org.jetbrains.compose.ui)
            implementation(libs.org.jetbrains.compose.foundation)
            implementation(libs.org.jetbrains.compose.animation)
            implementation(libs.org.jetbrains.compose.material3)
            implementation(libs.org.jetbrains.compose.material.icons.extended)
            implementation(libs.org.jetbrains.compose.ui.tooling.preview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation(libs.org.jetbrains.kotlinx.serialization.json)

            implementation(libs.org.jetbrains.navigation3.ui)
            implementation(libs.org.jetbrains.lifecycle.viewmodel.navigation3)

            implementation(libs.dev.zacsweers.metrox.viewmodel)
            implementation(libs.dev.zacsweers.metrox.viewmodel.compose)

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
            api(projects.feature.servicedetails)
            api(projects.feature.pinned)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
