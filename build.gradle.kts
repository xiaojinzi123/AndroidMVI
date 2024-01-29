buildscript {

    dependencies {
        classpath("com.android.tools.build:gradle:8.2.1")
    }

}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version libs.versions.agp apply false
    id("org.jetbrains.kotlin.android") version libs.versions.kotlin apply false
    id("com.android.library") version libs.versions.agp apply false
    id("org.jetbrains.kotlin.jvm") version libs.versions.kotlin apply false
    id("com.google.devtools.ksp") version libs.versions.ksp apply false
}