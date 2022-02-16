plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdk = 32
    buildToolsVersion = "32.0.0"
    defaultConfig {
        applicationId = "com.lt2333.simplicitytools"
        minSdk = 30
        targetSdk = 32
        versionCode = 3
        versionName = "1.0.2"
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            setProguardFiles(listOf(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"))
        }
    }
    compileOptions {
        sourceCompatibility  = JavaVersion.VERSION_1_8
        targetCompatibility  = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding  = true
    }
}

dependencies {
    compileOnly("de.robv.android.xposed:api:82")
    //compileOnly("de.robv.android.xposed:api:82:sources")
    //implementation("androidx.constraintlayout:constraintlayout:2.1.3")
    implementation("androidx.preference:preference-ktx:1.2.0")
    //implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.5.0")
}