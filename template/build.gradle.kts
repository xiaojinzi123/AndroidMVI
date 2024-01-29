plugins {
    id("commonModulePlugin")
}

android {
    namespace = "com.xiaojinzi.mvi.template"
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    api(project(":mvi"))
    api(libs.lottie.compose)
}