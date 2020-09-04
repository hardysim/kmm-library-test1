plugins {
    kotlin("multiplatform") version "1.4.0"
    id("com.android.library")
    id("kotlin-android-extensions")
    id("maven-publish")
}

group = "com.example.lib-test"
version = "1.0.0"




fun Project.envOrProperty(name: String): String? {
    return System.getenv(name) ?: project.propertyStringOrNull(name)
}

fun Project.propertyStringOrNull(propertyName: String): String? {
    return if (hasProperty(propertyName)) {
        property(propertyName) as? String

    } else {
        null
    }
}

val artifactoryUrl = project.envOrProperty("artifactory_url")
val artifactoryUser = project.envOrProperty("artifactory_username")
val artifactoryPassword = project.envOrProperty("artifactory_password")

publishing {
    repositories {
        maven {
            url = uri("${artifactoryUrl}/libs-release-local")
            credentials {
                username = "${artifactoryUser}"
                password = "${artifactoryPassword}"
            }
        }
    }
}



repositories {
    gradlePluginPortal()
    google()
    jcenter()
    mavenCentral()
}
kotlin {
    android{
        publishLibraryVariants("release", "debug")
    }

    iosX64("ios") {
        binaries {
            framework {
                baseName = "library"
            }
        }
    }
    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("androidx.core:core-ktx:1.2.0")
            }
        }
        val androidTest by getting
        val iosMain by getting
        val iosTest by getting
    }
}
android {
    compileSdkVersion(29)
    defaultConfig {
        minSdkVersion(24)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}
