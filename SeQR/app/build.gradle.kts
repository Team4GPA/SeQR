plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.seqr"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.seqr"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
    //if you add another subfolder in the res/layouts/... folder, you need
    //to add it to this section, relative to the build.gradle.kts file:
    //so, to add subfolder 'foo', add the line:
    //      res.srcDir("/src/main/res/layouts/foo/")
    //don't forget to add the "layout" (no 's') as a subfolder within that
    //to properly populate the resource lists for any R.layout.[...] call!
//    sourceSets{
//        getByName("main"){
//            java{
//                res.srcDir("/src/main/res/layouts/administrator")
//                res.srcDir("/src/main/res/layouts/announcements")
//                res.srcDir("/src/main/res/layouts/attendee")
//                res.srcDir("/src/main/res/layouts/events")
//                res.srcDir("/src/main/res/layouts/helpers")
//                res.srcDir("/src/main/res/layouts/organizer")
//                res.srcDir("/src/main/res/layouts/profile")
//                res.srcDir("/src/main/res/layouts/qr")
//                res.srcDir("/src/main/res/layouts/")
//                res.srcDir("/src/main/res/layout/")
//                res.srcDir("/src/main/res/")
//            }
//        }
//    }
}

dependencies {
    //implementation("androidx.slidingpanelayout:slidingpanelayout:1.2.0")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation("com.google.android.gms:play-services-code-scanner:16.1.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation(platform("com.google.firebase:firebase-bom:32.7.1"))
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.squareup.picasso:picasso:2.8")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("com.google.firebase:firebase-messaging")
    implementation("com.google.firebase:firebase-analytics")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    //image upload stuff
    //implementation("com.squareup.picasso:picasso:2.5.2")
    implementation("com.journeyapps:zxing-android-embedded:4.1.0")
    implementation("com.amplitude:android-sdk:2.34.1")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

}