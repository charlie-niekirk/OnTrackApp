plugins {
    id("ontrack.kmp.base.lint")
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.sqldelight)
    id("dev.zacsweers.metro")
}

onTrackKmp {
    namespace = "me.cniekirk.ontrackapp.core.database"
    frameworkBaseName = "core:databaseKit"
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(libs.app.cash.sqldelight.runtime)
                implementation(libs.org.jetbrains.kotlinx.serialization.json)

                implementation(projects.core.common)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.app.cash.sqldelight.android.driver)
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
                implementation(libs.app.cash.sqldelight.native.driver)
            }
        }
    }
}

sqldelight {
    databases {
        create("StationDatabase") {
            packageName.set("me.cniekirk.ontrackapp.core.database")
        }
    }
}
