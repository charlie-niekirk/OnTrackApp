plugins {
    id("ontrack.kmp.base.lint")
    alias(libs.plugins.kotlinSerialization)
    id("dev.zacsweers.metro")
}

onTrackKmp {
    namespace = "me.cniekirk.ontrackapp.core.datastore"
    frameworkBaseName = "core:datastoreKit"
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(libs.org.jetbrains.kotlinx.serialization.json)
                implementation(libs.androidx.datastore.core.okio)
                implementation(libs.squareup.okio)

                implementation(projects.core.common)
                implementation(projects.core.domain)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.org.jetbrains.kotlinx.coroutines.test)
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
