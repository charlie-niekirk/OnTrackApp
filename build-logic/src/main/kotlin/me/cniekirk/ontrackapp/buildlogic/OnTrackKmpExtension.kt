package me.cniekirk.ontrackapp.buildlogic

import org.gradle.api.provider.Property

abstract class OnTrackKmpExtension {
    abstract val namespace: Property<String>
    abstract val frameworkBaseName: Property<String>
    abstract val staticFramework: Property<Boolean>
}
