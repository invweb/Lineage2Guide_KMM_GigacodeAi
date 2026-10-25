plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.compose)
}

group = "com.zx_tole.lineage2_guide"

kotlin {
    jvm("desktop")

    sourceSets {
        val desktopMain = sourceSets.getByName("desktopMain")
        desktopMain.dependencies {
            implementation(project(":shared"))
            
            // Compose Desktop
            implementation("org.jetbrains.compose.foundation:foundation:1.7.0")
            implementation("org.jetbrains.compose.material:material-icons-extended:1.7.0")
            implementation("org.jetbrains.compose.material3:material3:1.7.0")
            implementation("org.jetbrains.compose.ui:ui:1.7.0")
            implementation("org.jetbrains.compose.desktop:desktop:1.7.0")
            implementation("org.jetbrains.skiko:skiko-awt-runtime-macos-arm64:0.8.15")
            
            // Koin for Desktop
            implementation(libs.koin.core)
            
            // Coroutines
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.10.2")
        }
    }
}

tasks.register<JavaExec>("runDesktop") {
    mainClass.set("com.zx_tole.lineage2_guide.DesktopAppKt")
    val desktopCompilation = kotlin.targets["desktop"].compilations["main"]
    classpath = files(desktopCompilation.output.classesDirs, desktopCompilation.runtimeDependencyFiles)
    enableAssertions = true
    
    // Extract Skia native library to temp dir
    val runtimeFiles = desktopCompilation.runtimeDependencyFiles?.files ?: emptySet()
    val skikoJar = runtimeFiles.find { it.name.contains("skiko-awt-runtime-macos-arm64") }
    if (skikoJar != null) {
        val tempDir = layout.buildDirectory.dir("skiko-native").get().asFile
        tempDir.mkdirs()
        copy {
            from(zipTree(skikoJar).matching { include("**/*.dylib") })
            into(tempDir)
        }
        jvmArgs("-Djava.library.path=${tempDir.absolutePath}")
    }
}
