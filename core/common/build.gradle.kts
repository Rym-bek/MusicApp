@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.org.jetbrains.kotlin.android)

    kotlin("kapt")
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.example.common"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
}

dependencies {
    implementation(project(":player"))
    implementation(project(":core:model"))

    //viewModel
    implementation(libs.lifecycle.viewmodel.compose)

    //hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    //media3
    implementation(libs.media3.session)

    val composeBom = platform(libs.compose.bom)

    //coil
    implementation(libs.coil.compose)

    //icons
    implementation(libs.material.icons.extended)

    implementation(composeBom)
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.material.icons.extended)
}

kapt {
    correctErrorTypes = true
}