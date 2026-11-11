# Safe Game Overlay

A small Android floating utility panel with a clock and a personal note field.

This app is intentionally **not** a game cheat: it does not inspect game memory, capture the screen, identify players, or interact with another app's data.

## Build APK with GitHub Actions

1. Open the **Actions** tab in this repository.
2. If GitHub asks, click **I understand my workflows, go ahead and enable them**.
3. Select **Build APK** and click **Run workflow** (or wait for the automatic run after a push).
4. Open the completed workflow run.
5. Under **Artifacts**, download **SafeGameOverlay-debug**.
6. Extract the ZIP; install `app-debug.apk` on your Android device.

## Android setup

On first launch, tap **Включить панель** and grant the Android **Display over other apps** permission. The notification indicates that the foreground service is running. Tap **Остановить панель** in the app or **Закрыть панель** in the floating panel to stop it.

## Project

- Kotlin, Android Gradle Plugin
- Min SDK 23
- GitHub Actions builds a debug APK
