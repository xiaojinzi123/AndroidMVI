import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class CommonModulePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            plugins.apply {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }
            extensions.configure<LibraryExtension> {
                compileSdk = 34
                defaultConfig {
                    minSdk = 24
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    consumerProguardFiles("consumer-rules.pro")
                }
                buildTypes {
                    release {
                        isMinifyEnabled = false
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro"
                        )
                    }
                }
                compileOptions {
                    sourceCompatibility(JavaVersion.VERSION_17)
                    targetCompatibility(JavaVersion.VERSION_17)
                }
                buildFeatures {
                    compose = true
                }
                composeOptions {
                    kotlinCompilerExtensionVersion =
                        libs.findVersion("kotlinCompilerExtensionVersion").get().toString()
                }
            }
        }
    }
}