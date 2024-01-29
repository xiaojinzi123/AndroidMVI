plugins {
    id("commonModulePlugin")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.xiaojinzi.demo.module.base"
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    api(project(":mvi"))
    api(project(":template"))

    api(libs.kcomponent.core)
    ksp(libs.kcomponent.compiler)

}