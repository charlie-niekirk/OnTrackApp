package me.cniekirk.ontrackapp.buildlogic

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.Framework
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

class OnTrackKmpBaseConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        val libs = libsCatalog()
        val compileSdk = libs.requiredVersion("android-compileSdk").toInt()
        val minSdk = libs.requiredVersion("android-minSdk").toInt()

        val extension = extensions.create<OnTrackKmpExtension>("onTrackKmp").apply {
            staticFramework.convention(false)
        }

        afterEvaluate {
            val namespace = extension.namespace.orNull?.trim().takeUnless { it.isNullOrEmpty() }
                ?: error("${project.path}: onTrackKmp.namespace is required")
            val frameworkBaseName = extension.frameworkBaseName.orNull?.trim().takeUnless { it.isNullOrEmpty() }
                ?: error("${project.path}: onTrackKmp.frameworkBaseName is required")
            val staticFramework = extension.staticFramework.get()

            extensions.configure<KotlinMultiplatformExtension>("kotlin") {
                targets.named("android", KotlinMultiplatformAndroidLibraryTarget::class.java).configure {
                    this.namespace = namespace
                }
                configureIosFrameworks(this, frameworkBaseName, staticFramework)
            }
        }

        pluginManager.apply("org.jetbrains.kotlin.multiplatform")
        pluginManager.apply("com.android.kotlin.multiplatform.library")

        extensions.configure<KotlinMultiplatformExtension>("kotlin") {
            targets.named("android", KotlinMultiplatformAndroidLibraryTarget::class.java).configure {
                this.compileSdk = compileSdk
                this.minSdk = minSdk

                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_11)
                }

                withHostTestBuilder {}

                withDeviceTestBuilder {
                    sourceSetTreeName = "test"
                }.configure {
                    instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }
            }

            iosArm64().binaries.framework {}
            iosSimulatorArm64().binaries.framework {}
        }
    }

    private fun configureIosFrameworks(
        kotlin: KotlinMultiplatformExtension,
        frameworkBaseName: String,
        staticFramework: Boolean,
    ) {
        kotlin.targets.withType(KotlinNativeTarget::class.java).configureEach {
            binaries.withType(Framework::class.java).configureEach {
                baseName = frameworkBaseName
                isStatic = staticFramework
            }
        }
    }
}
