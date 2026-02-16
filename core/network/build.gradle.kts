plugins {
    id("ontrack.kmp.base.lint")
    alias(libs.plugins.kotlinSerialization)
    id("dev.zacsweers.metro")
}

onTrackKmp {
    namespace = "me.cniekirk.ontrackapp.core.network"
    frameworkBaseName = "core:networkKit"
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(libs.io.ktor.client.core)
                implementation(libs.io.ktor.client.auth)
                implementation(libs.io.ktor.client.content.negotiation)
                implementation(libs.io.ktor.client.logging)
                implementation(libs.io.ktor.serialization.kotlinx.json)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.io.ktor.client.okhttp)
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
                implementation(libs.io.ktor.client.darwin)
            }
        }
    }
}
