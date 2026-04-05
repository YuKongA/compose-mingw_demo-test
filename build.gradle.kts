plugins {
    kotlin("multiplatform") version "2.3.20"
    kotlin("plugin.compose") version "2.3.20"
}

repositories {
    mavenLocal()
    mavenCentral()
    google()
    maven("https://packages.jetbrains.team/maven/p/cmp/dev")
}

kotlin {
    mingwX64("mingw") {
        binaries {
            executable {
                entryPoint = "main"
                linkerOpts("-mwindows") // Windows GUI subsystem, no console window
            }
        }
    }

    sourceSets {
        val mingwMain by getting {
            dependencies {
                implementation("org.jetbrains.compose.material3:material3-mingwx64:1.5.0-alpha13")
                implementation("org.jetbrains.compose.ui:ui-mingwx64:1.11.0-alpha04")
                implementation("org.jetbrains.compose.foundation:foundation-mingwx64:1.11.0-alpha04")
                implementation("org.jetbrains.compose.runtime:runtime-mingwx64:1.11.0-alpha04")
                implementation("org.jetbrains.skiko:skiko:0.144.5")
            }
        }
    }
}

// Copy ANGLE runtime DLLs (libEGL.dll, libGLESv2.dll) alongside the executable
val angleDllDir = layout.projectDirectory.dir("libs")
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinNativeLink>().configureEach {
    val dllFiles = angleDllDir.asFile.listFiles()?.filter { it.extension == "dll" } ?: emptyList()
    doLast {
        val outputDir = outputFile.get().parentFile
        dllFiles.forEach { it.copyTo(outputDir.resolve(it.name), overwrite = true) }
    }
}
