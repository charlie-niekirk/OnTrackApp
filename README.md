This is a Kotlin Multiplatform project targeting Android, iOS.

* [/composeApp](./composeApp/src) is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - [commonMain](./composeApp/src/commonMain/kotlin) is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    the [iosMain](./composeApp/src/iosMain/kotlin) folder would be the right place for such calls.
    Similarly, if you want to edit the Desktop (JVM) specific part, the [jvmMain](./composeApp/src/jvmMain/kotlin)
    folder is the appropriate location.

* [/iosApp](./iosApp/iosApp) contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform,
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

### Build Logic

This project uses binary class-based convention plugins from [`/build-logic`](./build-logic).

Plugin IDs:
- `ontrack.kmp.base`
- `ontrack.kmp.base.lint`
- `ontrack.kmp.compose`
- `ontrack.android.application`

`onTrackKmp` fields:
- `namespace` (required)
- `frameworkBaseName` (required)
- `staticFramework` (optional, defaults to `false`)

How to choose:
- Use `ontrack.kmp.base.lint` for KMP modules that do not need Compose resources/tooling runtime classpath wiring.
- Add `ontrack.kmp.compose` for KMP modules that use Compose resources/UI.
- Use `ontrack.kmp.base` (without lint) only when lint wiring is intentionally omitted.
- Use `ontrack.android.application` for Android app modules to share Java/Kotlin toolchain and packaging defaults.

### Build and Run Android Application

To build and run the development version of the Android app, use the run configuration from the run widget
in your IDE’s toolbar or build it directly from the terminal:
- on macOS/Linux
  ```shell
  ./gradlew :composeApp:assembleDebug
  ```
- on Windows
  ```shell
  .\gradlew.bat :composeApp:assembleDebug
  ```

### Build and Run iOS Application

To build and run the development version of the iOS app, use the run configuration from the run widget
in your IDE’s toolbar or open the [/iosApp](./iosApp) directory in Xcode and run it from there.

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…
