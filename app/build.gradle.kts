plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)

}

android {
    namespace = "com.example.touchapp"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.example.touchapp"
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
    buildFeatures
    run {
        viewBinding
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.ucrop.v226)
    implementation(libs.play.services.fitness)
    implementation(libs.circleimageview)
    implementation(libs.picasso)
    implementation(libs.dhaval2404.imagepicker)
    testImplementation(libs.junit)
    implementation(libs.firebase.analytics)
    implementation(platform(libs.firebase.bom))
    androidTestImplementation(libs.ext.junit)
    implementation(libs.glide)
    annotationProcessor(libs.compiler)
    implementation(libs.firebase.storage.v2111) // Ensure this is the correct version
    implementation(libs.firebase.core) // Ensure this is the correct version
    androidTestImplementation(libs.espresso.core)
}
