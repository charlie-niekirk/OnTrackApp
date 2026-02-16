plugins {
    id("ontrack.kmp.base.lint")
    id("dev.zacsweers.metro")
}

onTrackKmp {
    namespace = "me.cniekirk.ontrackapp.core.data"
    frameworkBaseName = "core:dataKit"
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(libs.org.jetbrains.kotlinx.coroutines.core)

                implementation(projects.core.datastore)
                implementation(projects.core.database)
                implementation(projects.core.domain)
                implementation(projects.core.network)
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
