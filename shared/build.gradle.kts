import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.androidKotlinMultiplatformLibrary)  // NOT androidLibrary
  alias(libs.plugins.composeMultiplatform)
  alias(libs.plugins.composeCompiler)
  alias(libs.plugins.ktlint)
}


kotlin {
  compilerOptions {
    freeCompilerArgs.add("-Xexpect-actual-classes")
  }
  applyDefaultHierarchyTemplate()

  // Android
  android {
    namespace = "com.kmptemplate.library"
    compileSdk = libs.versions.android.max.sdk.get().toInt()
    minSdk = libs.versions.android.min.sdk.get().toInt()

    androidResources {
      enable = true
    }

    withHostTest {
      isIncludeAndroidResources = true
    }

    withDeviceTest {
      instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
      execution = "HOST"
    }
  }

  // iOS
  listOf(
    iosArm64(),
    iosSimulatorArm64(),
  ).forEach { target ->
    target.binaries.framework {
      baseName = "Shared" // import Shared in ContentView.swift
      isStatic = true
      binaryOption("bundleId", "com.kmptemplate.shared")
    }
  }



  // Web
  listOf(
    js(),
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs(),
  ).forEach { target ->
    target.browser()
  }

  // Common
  sourceSets {
    commonMain.dependencies {
      api(libs.compose.runtime)
      api(libs.compose.foundation)
      api(libs.compose.material3)
      api(libs.compose.ui)
      api(libs.compose.components.resources)
      api(libs.androidx.lifecycle.viewmodelCompose)
      api(libs.androidx.lifecycle.runtimeCompose)
    }
    commonTest.dependencies {
      api(libs.kotlin.test)
    }
    androidMain.dependencies {
      api(libs.compose.uiToolingPreview)  // ADD: Android only
      api(project.dependencies.platform("com.google.firebase:firebase-bom:33.7.0"))
      api("com.google.firebase:firebase-auth-ktx")
    }
    create("nonAndroidMain") {
      dependsOn(commonMain.get())
      iosMain.get().dependsOn(this)
      jsMain.get().dependsOn(this)
    }

    // Firebase for non-Android, non-WASM targets
    val firebaseMain by creating {
      dependsOn(commonMain.get())
      androidMain.get().dependsOn(this)
      iosMain.get().dependsOn(this)
      jsMain.get().dependsOn(this)
      dependencies {
        api("dev.gitlive:firebase-auth:2.4.0")
        api("dev.gitlive:firebase-common:2.4.0")
      }
    }

    // WASM stub — no Firebase
    wasmJsMain.dependencies {
      // Firebase not supported on WASM yet
    }
  }
}
