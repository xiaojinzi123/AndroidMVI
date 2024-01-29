plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin.api)
    compileOnly(libs.kotlin.gradlePlugin)
}


gradlePlugin {
    plugins {
        register("commonModulePlugin") {
            id = "commonModulePlugin"
            implementationClass = "CommonModulePlugin"
        }
    }
}