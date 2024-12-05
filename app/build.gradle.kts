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

    packaging {
        resources {
            excludes.addAll(
                listOf(
                    "META-INF/LICENSE.md", // Exclude LICENSE.md
                    "META-INF/LICENSE.txt", // Exclude LICENSE.txt
                    "META-INF/NOTICE.md", // Exclude NOTICE.md
                    "META-INF/NOTICE.txt", // Exclude NOTICE.txt
                    "META-INF/DEPENDENCIES" // Exclude other redundant files
                )
            )
        }
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

    implementation(libs.recyclerview)
    implementation(libs.cardview)
    implementation(libs.osmdroid)

    implementation(libs.okhttp) // Added for OkHttp


    implementation(libs.glide)
    implementation(libs.lottie)

    implementation(libs.javamail)
    implementation(libs.activation)



}
