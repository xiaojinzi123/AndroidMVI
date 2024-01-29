plugins {
    id("commonModulePlugin")
}

android {
    namespace = "com.xiaojinzi.demo.lib.res"
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}