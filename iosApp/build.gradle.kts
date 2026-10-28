plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.kotlin.compose)
}

kotlin {
    android {
        namespace = "com.zx_tole.lineage2_guide.shared"
        compileSdk = 37
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                // Compose Multiplatform
                implementation("org.jetbrains.compose.foundation:foundation:1.7.0")
                implementation("org.jetbrains.compose.material3:material3:1.7.0")
                implementation("org.jetbrains.compose.material:material-icons-extended:1.7.0")
                implementation("org.jetbrains.compose.ui:ui:1.7.0")
                
                // Coroutines
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.2")
                
                // DI
                implementation(libs.koin.core)
                
                // Network
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.kotlinx.serialization.json)
                
                // Koin Compose
                implementation("io.insert-koin:koin-compose:4.0.0")
            }
        }
        
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        
        val iosMain by creating {
            dependsOn(commonMain)
            
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        
        val androidMain by getting {
            dependencies {
                implementation(libs.ktor.client.okhttp)
                implementation(libs.koin.android)
            }
        }
    }
}
