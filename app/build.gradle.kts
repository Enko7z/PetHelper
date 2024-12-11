plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    // ROOM
    id ("kotlin-kapt")
}

android {
    namespace = "com.example.pethelper"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.pethelper"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        //ROOM
        kapt {
            arguments {arg("room.schemaLocation", "$projectDir/schemas")}
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
    buildFeatures {
        dataBinding = true;
    }
    dataBinding{
        enable = true;
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    //CALENDAR
    implementation ("com.applandeo:material-calendar-view:1.9.2")
    // ROOM
    implementation ("androidx.room:room-runtime:2.5.0") // Библиотека "Room"
    kapt ("androidx.room:room-compiler:2.5.0") // Кодогенератор
    implementation ("androidx.room:room-ktx:2.5.0") // Дополнительно для Kotlin Coroutines, Kotlin Flows

    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4") // Kotlin Coroutines
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    implementation ("androidx.activity:activity-ktx:1.6.1")

}