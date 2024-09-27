plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    id("kotlin-kapt")
}

android {
    namespace = "com.buildbyhirenp.freshveggiemart"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.buildbyhirenp.freshveggiemart"
        minSdk = 26
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
    buildFeatures{
        dataBinding = true
        viewBinding = true
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.firestore)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.google.firebase.auth)

    // Text Dimentions
    implementation(libs.ssp.android)
    implementation(libs.sdp.android)

    // Naigation Components
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    implementation(libs.ccp.v260) // for country code picker
    implementation(libs.chaosleung.pinview)   // for OTP Verification UI

    //retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)


    implementation(libs.volley)  // volly

    //room database
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    kapt("androidx.room:room-compiler:2.6.1")

    //glide
    implementation(libs.glide)
    annotationProcessor (libs.compiler)

//    implementation(libs.intentsdk.v243)  // PhonePe
//    implementation(libs.appinvokesdk)  // paytm
    implementation(libs.checkout)  // razorpay


    implementation(libs.duo.navigation.drawer)   //  for drawer ui
    implementation(libs.materialdialog)  // for bottom sheet dialog
    implementation(libs.imageslideshow) // for slider
    implementation(libs.shimmer)  // for shimmer on ui
}