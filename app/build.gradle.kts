import com.android.build.gradle.internal.api.BaseVariantOutputImpl

plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdk = 32
    buildToolsVersion = "32.0.0"
    defaultConfig {
        applicationId = "com.lt2333.simplicitytools"
        minSdk = 31
        targetSdk = 32
        versionCode = 40
        versionName = "1.3.9"
        externalNativeBuild {
            cmake {
                targets("DexBuilder")
                abiFilters("arm64-v8a")
                cppFlags("-std=c++17")
                cFlags("-std=gnu99")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            setProguardFiles(
                listOf(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.majorVersion
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/**"
            excludes += "/kotlin/**"
            excludes += "/*.txt"
            excludes += "/*.bin"
        }
        dex {
            useLegacyPackaging = true
        }
    }
    applicationVariants.all {
        outputs.all {
            (this as BaseVariantOutputImpl).outputFileName =
                "Simplicity_Tools_Xposed-$versionName-$name.apk"
        }
    }
    androidResources {
        noCompress("libDexBuilder.so")
    }
    externalNativeBuild {
        cmake {
            path("src/main/cpp/CMakeLists.txt")
        }
    }
    ndkVersion = "23.1.7779620"
}

dependencies {
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")
    //API
    compileOnly("de.robv.android.xposed:api:82")
    //UI
    implementation(project(":blockmiui"))

    val appCenterSdkVersion = "4.3.1"
    implementation("com.microsoft.appcenter:appcenter-analytics:${appCenterSdkVersion}")
    implementation("com.microsoft.appcenter:appcenter-crashes:${appCenterSdkVersion}")

    implementation("com.github.kyuubiran:EzXHelper:0.7.4")

}