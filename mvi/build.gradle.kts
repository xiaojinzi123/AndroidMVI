plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    // id("common-lib-plugin")
}

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
    api(Libs.xiaojinzi_android_support_ktx)
    api(Libs.xiaojinzi_android_support_annotation)
    api(Libs.xiaojinzi_android_support_compose)
    api(Libs.compose_runtime)
    api(Libs.compose_runtime_android)
    api(Libs.compose_ui_android)
    api(Libs.compose_foundation_android)
    api(Libs.lifecycle_viewmodel_compose)
    api(Libs.androidx_core)
    api(Libs.androidx_appcompat)

}