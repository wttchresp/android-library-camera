plugins {
  id("com.android.library")
  id("kotlin-android")
}

android {
  compileSdkVersion(30)
  buildToolsVersion("30.0.2")

  defaultConfig {
    minSdkVersion(22)
    targetSdkVersion(30)
    versionCode(1)
    versionName("1.0")

    testInstrumentationRunner("androidx.test.runner.AndroidJUnitRunner")
    consumerProguardFiles("consumer-rules.pro")
  }

  buildTypes {
    getByName("release") {
      minifyEnabled(false)
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }
  sourceSets {
    getByName("main") {
      java.srcDirs("src/main/java", "src/main/kotlin")
    }
  }
}

dependencies {
  val kotlinVersion: String by parent!!.extra
  implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
  implementation("androidx.core:core-ktx:1.3.2")
  implementation("androidx.appcompat:appcompat:1.2.0")
  implementation("com.google.android.material:material:1.2.1")


  // CameraX core library using camera2 implementation
  api("androidx.camera:camera-camera2:1.0.0-rc01")
  // CameraX Lifecycle Library
  api("androidx.camera:camera-lifecycle:1.0.0-rc01")
  // CameraX View class
  api("androidx.camera:camera-view:1.0.0-alpha20")

  implementation(project(":android-library-core"))

  testImplementation("junit:junit:4.13.1")
  androidTestImplementation("androidx.test.ext:junit:1.1.2")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
}