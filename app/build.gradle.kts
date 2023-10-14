plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.meettime"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.meettime"
        minSdk = 23
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    //Default Dependencies
//    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0-alpha13")
    implementation ("com.google.firebase:firebase-core:21.1.1")
    implementation ("com.google.firebase:firebase-database:20.3.0")
    implementation ("com.google.firebase:firebase-storage:20.3.0")
    implementation ("com.google.firebase:firebase-auth:22.2.0")
    implementation ("com.firebaseui:firebase-ui-database:8.0.2")
    implementation(platform("com.google.firebase:firebase-bom:32.5.0"))
    implementation ("com.google.android.material:material:1.11.0-beta01")
    implementation("androidx.navigation:navigation-fragment:2.7.5")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation ("com.opentok.android:opentok-android-sdk:2.26.1")
    implementation ("pub.devrel:easypermissions:3.0.0")
    implementation ("com.squareup.picasso:picasso:2.71828")
    implementation("junit:junit:4.13.2")
    implementation("androidx.appcompat:appcompat:1.7.0-alpha03")
    implementation("androidx.navigation:navigation-ui:2.7.5")
    implementation("androidx.test.ext:junit:1.2.0-alpha01")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.0-alpha01")


}