plugins {
    id("ontrack.kmp.base.lint")
    id("ontrack.kmp.compose")
    alias(libs.plugins.kotlinSerialization)
    id("dev.zacsweers.metro")
}

onTrackKmp {
    namespace = "me.cniekirk.ontrackapp.feature.home"
    frameworkBaseName = "feature:homeKit"
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)

                implementation(libs.androidx.lifecycle.viewmodelCompose)

                implementation(libs.org.jetbrains.compose.runtime)
                implementation(libs.org.jetbrains.compose.ui)
                implementation(libs.org.jetbrains.compose.foundation)
                implementation(libs.org.jetbrains.compose.animation)
                implementation(libs.org.jetbrains.compose.material3)
                implementation(libs.org.jetbrains.compose.material.icons.extended)
                implementation(libs.org.jetbrains.compose.ui.tooling.preview)
                implementation(libs.org.jetbrains.compose.components.resources)

                implementation(libs.org.jetbrains.navigation3.ui)
                implementation(libs.org.jetbrains.lifecycle.viewmodel.navigation3)
                implementation(libs.org.jetbrains.kotlinx.datetime)

                implementation(libs.org.orbit.mvi.core)
                implementation(libs.org.orbit.mvi.compose)
                implementation(libs.org.orbit.mvi.viewmodel)

                implementation(libs.dev.zacsweers.metrox.viewmodel)
                implementation(libs.dev.zacsweers.metrox.viewmodel.compose)

                implementation(libs.co.touchlab.kermit)

                implementation(projects.core.domain)
                implementation(projects.core.common)
                implementation(projects.core.designsystem)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.org.jetbrains.kotlinx.coroutines.test)
                implementation(libs.org.orbit.mvi.test)
            }
        }

        androidMain {
            dependencies {
            }
        }

        getByName("androidDeviceTest") {
            dependencies {
                implementation(libs.androidx.runner)
                implementation(libs.androidx.core)
                implementation(libs.androidx.testExt.junit)
            }
        }

        iosMain {
            dependencies {
            }
        }
    }
}
