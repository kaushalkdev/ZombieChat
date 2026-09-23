plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.firebase.perf)
}

android {
    namespace = "com.example.zombiechat"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.example.zombiechat"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
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
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // AndroidX & UI
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.material)
    implementation(libs.circleimageview)
    implementation(libs.picasso)

    // Lifecycle
    implementation(libs.bundles.androidx.lifecycle)

    // Google Auth & Credentials
    implementation(libs.play.services.auth)
    implementation(libs.bundles.androidx.credentials)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase.sdks)
    implementation(libs.firebase.perf)
    implementation(libs.firebase.ui.firestore)

    // Dependency Injection
    implementation(libs.bundles.koin)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
