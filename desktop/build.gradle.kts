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
            
            // Koin for Desktop (koin.compose depends on AndroidX, not compatible with Desktop)
            implementation(libs.koin.core)
            
            // Coroutines
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.10.2")
        }
    }
}

application {
    mainClass.set("com.zx_tole.lineage2_guide.DesktopAppKt")
}
