plugins {
    id("com.android.application")
}

android {
    namespace = "com.i4season.i4season_camera"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.i4season.i4season_camera"
        minSdk = 21
        targetSdk = 33
        versionCode = 44
        versionName = "1.0.062-reconstructed"

        ndk {
            abiFilters += listOf("arm64-v8a", "armeabi-v7a")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
