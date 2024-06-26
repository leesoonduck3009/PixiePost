plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.pixelpost"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.pixelpost"
        minSdk = 26
        targetSdk = 33
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
    packagingOptions {
        exclude("META-INF/NOTICE.md")
        exclude("META-INF/LICENSE.md")

        buildFeatures {
            viewBinding = true
        }
    }
    buildFeatures {
        viewBinding = true
    }

    dependencies {
        val camerax_version = "1.3.2"
        implementation("androidx.camera:camera-core:${camerax_version}")
        implementation("androidx.camera:camera-camera2:${camerax_version}")
        implementation("androidx.camera:camera-lifecycle:${camerax_version}")
        implementation("androidx.camera:camera-video:${camerax_version}")
        implementation("androidx.camera:camera-view:${camerax_version}")
        implementation("androidx.camera:camera-extensions:${camerax_version}")

        implementation("com.github.bumptech.glide:glide:4.15.1")
        annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")
        implementation("com.makeramen:roundedimageview:2.3.0")

        implementation("androidx.appcompat:appcompat:1.6.1")
        implementation("com.google.android.material:material:1.11.0")

        implementation("com.hbb20:ccp:2.5.3")
        implementation("androidx.constraintlayout:constraintlayout:2.1.4")
        implementation("com.google.firebase:firebase-firestore:24.10.3")
        implementation("com.google.firebase:firebase-storage:20.3.0")
        implementation(platform("com.google.firebase:firebase-bom:32.7.4"))
        implementation("com.google.firebase:firebase-auth")
        implementation("androidx.activity:activity:1.8.0")
        testImplementation("junit:junit:4.13.2")
        androidTestImplementation("androidx.test.ext:junit:1.1.5")
        androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
        implementation("com.sun.mail:android-mail:1.6.7")
        implementation("com.sun.mail:android-activation:1.6.7")
        implementation("com.google.zxing:core:3.5.2")
        // ViewModel and LiveData
        implementation ("androidx.lifecycle:lifecycle-livedata:2.7.0")
        implementation ("androidx.lifecycle:lifecycle-viewmodel:2.7.0")

        implementation("androidx.viewpager2:viewpager2:1.0.0")

        implementation ("com.github.bumptech.glide:glide:4.12.0")
        annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")
        implementation ("com.makeramen:roundedimageview:2.3.0")
        // Use this dependency to bundle the model with your app
        implementation ("com.google.mlkit:barcode-scanning:17.2.0")

    }
}
dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.activity:activity:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
}
