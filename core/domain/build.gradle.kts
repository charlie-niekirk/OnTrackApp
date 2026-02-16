plugins {
    id("ontrack.kmp.base.lint")
    alias(libs.plugins.kotlinSerialization)
}

onTrackKmp {
    namespace = "me.cniekirk.ontrackapp.core.domain"
    frameworkBaseName = "core:domainKit"
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(libs.org.jetbrains.kotlinx.coroutines.core)
                implementation(libs.org.jetbrains.kotlinx.serialization.json)
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
