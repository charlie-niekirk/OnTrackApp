package me.cniekirk.ontrackapp.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project

class OnTrackKmpBaseLintConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("ontrack.kmp.base")
        pluginManager.apply("com.android.lint")
    }
}
