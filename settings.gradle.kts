pluginManagement {
    repositories {
        maven {
            url = uri("https://jitpack.io")
        }
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("android_support_version", "v1.0-beta208")
            library("android-support-ktx", "com.github.xiaojinzi123.AndroidSupport", "lib-ktx").versionRef("android_support_version")
            library("android-support-annotation", "com.github.xiaojinzi123.AndroidSupport", "lib-annotation").versionRef("android_support_version")
        }
    }
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven {
            url = uri("https://jitpack.io")
        }
        google()
        mavenCentral()
    }
}

rootProject.name = "AndroidMVI"
include(":app")
include(":mvi")
include(":template")
