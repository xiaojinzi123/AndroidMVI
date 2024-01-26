plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
}

gradlePlugin {
    plugins {
        register("common-lib-plugin") {
            id = "common-lib-plugin"
            implementationClass = "CommonLibPlugin"
        }
    }
}