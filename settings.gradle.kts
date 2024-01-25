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
            version("core", "1.12.0")
            library("core", "androidx.core", "core-ktx").versionRef("core")
            version("appcompat", "1.6.1")
            library("appcompat", "androidx.appcompat", "appcompat").versionRef("appcompat")
            version("android_support_version", "v1.0-beta208")
            library("android-support-ktx", "com.github.xiaojinzi123.AndroidSupport", "lib-ktx").versionRef("android_support_version")
            library("android-support-annotation", "com.github.xiaojinzi123.AndroidSupport", "lib-annotation").versionRef("android_support_version")
            library("android-support-compose", "com.github.xiaojinzi123.AndroidSupport", "lib-compose").versionRef("android_support_version")
            version("compose-bom", "2023.10.01")
            library("compose-bom", "androidx.compose", "compose-bom").versionRef("compose-bom")
            version("compose", "1.5.4")
            library("compose-runtime", "androidx.compose.runtime", "runtime").versionRef("compose")
            library("compose-runtime-android", "androidx.compose.runtime", "runtime-android").versionRef("compose")
            library("compose-ui-android", "androidx.compose.ui", "ui-android").versionRef("compose")
            library("compose-foundation-android", "androidx.compose.foundation", "foundation-layout-android").versionRef("compose")
            version("lifecycle", "2.6.2")
            library("lifecycle-viewmodel-compose", "androidx.lifecycle", "lifecycle-viewmodel-compose").versionRef("lifecycle")
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
