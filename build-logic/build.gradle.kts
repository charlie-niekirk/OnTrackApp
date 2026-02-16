plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

group = "me.cniekirk.ontrackapp.buildlogic"

dependencies {
    implementation("com.android.tools.build:gradle-api:${libs.versions.agp.get()}")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${libs.versions.kotlin.get()}")
}

gradlePlugin {
    plugins {
        register("onTrackKmpBase") {
            id = "ontrack.kmp.base"
            implementationClass = "me.cniekirk.ontrackapp.buildlogic.OnTrackKmpBaseConventionPlugin"
        }
        register("onTrackKmpBaseLint") {
            id = "ontrack.kmp.base.lint"
            implementationClass = "me.cniekirk.ontrackapp.buildlogic.OnTrackKmpBaseLintConventionPlugin"
        }
        register("onTrackKmpCompose") {
            id = "ontrack.kmp.compose"
            implementationClass = "me.cniekirk.ontrackapp.buildlogic.OnTrackKmpComposeConventionPlugin"
        }
        register("onTrackAndroidApplication") {
            id = "ontrack.android.application"
            implementationClass = "me.cniekirk.ontrackapp.buildlogic.OnTrackAndroidApplicationConventionPlugin"
        }
    }
}
