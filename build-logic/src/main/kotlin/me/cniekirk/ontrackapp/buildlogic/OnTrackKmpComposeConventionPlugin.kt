package me.cniekirk.ontrackapp.buildlogic

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class OnTrackKmpComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        val libs = libsCatalog()

        pluginManager.apply("org.jetbrains.compose")
        pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

        extensions.configure<KotlinMultiplatformExtension>("kotlin") {
            targets.named("android", KotlinMultiplatformAndroidLibraryTarget::class.java).configure {
                experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true
            }
        }

        afterEvaluate {
            dependencies.add(
                "androidRuntimeClasspath",
                libs.requiredLibrary("org-jetbrains-compose-ui-tooling").get(),
            )
            dependencies.add(
                "androidRuntimeClasspath",
                libs.requiredLibrary("org-jetbrains-compose-components-resources").get(),
            )
        }
    }
}
