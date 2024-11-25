plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.swiftride"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.swiftride"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(libs.recyclerview)  // Correctly points to version catalog
    implementation(libs.cardview)  // Ensure you have the correct reference here
    implementation(libs.osmdroid)  // Add the OSMDroid dependency here

    implementation(libs.glide)        // For image loading and caching
    implementation(libs.lottie)       // For animations and enhanced UI/UX

}