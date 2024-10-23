plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.devtools.ksp)
  alias(libs.plugins.jetbrains.compose.compiler)
  alias(libs.plugins.jetbrains.kotlin.android)
  alias(libs.plugins.jetbrains.kotlin.serialization)
  alias(libs.plugins.hilt.android)
}

android {
  namespace = "dev.ybrmst.dicodingevents"
  compileSdk = 34

  defaultConfig {
    applicationId = "dev.ybrmst.dicodingevents"
    minSdk = 24
    targetSdk = 34
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    vectorDrawables {
      useSupportLibrary = true
    }
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
    isCoreLibraryDesugaringEnabled = true

    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }

  kotlinOptions {
    jvmTarget = JavaVersion.VERSION_17.toString()
  }

  buildFeatures {
    compose = true
  }

  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }
}

dependencies {

  implementation(libs.kotlinx.serialization.json)
  implementation(libs.kotlinx.datetime)

  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.activity.compose)
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.compose.ui)
  implementation(libs.androidx.compose.ui.graphics)
  implementation(libs.androidx.compose.ui.tooling.preview)
  implementation(libs.androidx.compose.material3)
  implementation(libs.androidx.compose.ui.text.google.fonts)
  implementation(libs.androidx.compose.material.iconsExtended)
  implementation(libs.androidx.navigation.compose)
  implementation(libs.androidx.datastore.preferences)

  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.compose.ui.test.junit4)
  debugImplementation(libs.androidx.compose.ui.tooling)
  debugImplementation(libs.androidx.compose.ui.test.manifest)

  implementation(libs.androidx.room.runtime)
  ksp(libs.androidx.room.compiler)
  implementation(libs.androidx.room)

  implementation(libs.hilt)
  ksp(libs.hilt.compiler)
  implementation(libs.hilt.navigation.compose)

  implementation(libs.okhttp)
  implementation(libs.okhttp.loggingInterceptor)
  implementation(libs.retrofit)
  implementation(libs.retrofit.kotlinx.serialization)

  implementation(libs.coil)
  implementation(libs.coil.networkOkhttp)
  coreLibraryDesugaring(libs.android.desugar.jdk)
}