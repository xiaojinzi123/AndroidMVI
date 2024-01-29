plugins {
    id("commonModulePlugin")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.xiaojinzi.demo.module.base"
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

ksp {
    arg("ModuleName", "module_base")
}

dependencies {

    api(project(":mvi"))
    api(project(":template"))

    api(libs.kcomponent.core)
    ksp(libs.kcomponent.compiler)

}