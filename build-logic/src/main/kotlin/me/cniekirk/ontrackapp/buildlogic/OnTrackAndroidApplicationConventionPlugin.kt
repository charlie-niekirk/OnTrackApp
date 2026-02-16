package me.cniekirk.ontrackapp.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

class OnTrackAndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        val libs = libsCatalog()
        val compileSdk = libs.requiredVersion("android-compileSdk").toInt()
        val minSdk = libs.requiredVersion("android-minSdk").toInt()
        val targetSdk = libs.requiredVersion("android-targetSdk").toInt()

        pluginManager.apply("com.android.application")

        extensions.configure<ApplicationExtension>("android") {
            this.compileSdk = compileSdk

            defaultConfig {
                this.minSdk = minSdk
                this.targetSdk = targetSdk
            }

            packaging {
                resources {
                    excludes += "/META-INF/{AL2.0,LGPL2.1}"
                }
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_11
                targetCompatibility = JavaVersion.VERSION_11
            }
        }

        tasks.withType<KotlinJvmCompile>().configureEach {
            compilerOptions.jvmTarget.set(JvmTarget.JVM_11)
        }
    }
}
