plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.safeoverlay"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.safeoverlay"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
}
