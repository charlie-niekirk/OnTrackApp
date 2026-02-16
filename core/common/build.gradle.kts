plugins {
    id("ontrack.kmp.base.lint")
    id("ontrack.kmp.compose")
    id("dev.zacsweers.metro")
}

onTrackKmp {
    namespace = "me.cniekirk.ontrackapp.core.common"
    frameworkBaseName = "core:dataKit"
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(libs.org.jetbrains.compose.runtime)
                implementation(libs.org.jetbrains.compose.ui)
                implementation(libs.org.jetbrains.compose.foundation)

                implementation(libs.dev.zacsweers.metrox.viewmodel)
                implementation(libs.dev.zacsweers.metrox.viewmodel.compose)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
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
