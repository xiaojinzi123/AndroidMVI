plugins {
    id("commonDemoModulePlugin")
}

android {
    namespace = "com.xiaojinzi.demo.module.base"
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}