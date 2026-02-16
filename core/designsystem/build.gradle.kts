plugins {
    id("ontrack.kmp.base.lint")
    id("ontrack.kmp.compose")
}

onTrackKmp {
    namespace = "me.cniekirk.ontrackapp.core.designsystem"
    frameworkBaseName = "core:designsystemKit"
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(libs.org.jetbrains.compose.runtime)
                implementation(libs.org.jetbrains.compose.ui)
                implementation(libs.org.jetbrains.compose.foundation)
                implementation(libs.org.jetbrains.compose.material3)
                implementation(libs.org.jetbrains.compose.ui.tooling.preview)
                implementation(libs.org.jetbrains.compose.components.resources)

                implementation(projects.core.domain)
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
