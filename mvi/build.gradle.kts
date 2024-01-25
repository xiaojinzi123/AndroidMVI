plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

apply(from = File(rootProject.projectDir, "lib.gradle.kts").path)

android {
    namespace = "com.xiaojinzi.mvi"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_17)
        targetCompatibility(JavaVersion.VERSION_17)
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
        kotlinCompilerVersion = "1.9.0"
    }
}

dependencies {

    implementation(kotlin("reflect"))

    api(libs.android.support.ktx)
    api(libs.android.support.annotation)
    api(libs.android.support.compose)
    api(libs.compose.runtime)
    api(libs.compose.runtime.android)
    api(libs.compose.ui.android)
    api(libs.compose.foundation.android)
    api(libs.lifecycle.viewmodel.compose)
    api(libs.core)
    api(libs.appcompat)

}