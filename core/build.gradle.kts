plugins {
    id(BuildPlugins.androidLibrary)
    id(BuildPlugins.kotlinAndroid)
    id(BuildPlugins.kotlinAndroidExtensions)
    id(BuildPlugins.kapt)
}

android {
    compileSdkVersion(AndroidSdk.compileSdkVersion)

    android.buildFeatures.dataBinding = true
    android.buildFeatures.viewBinding = true

    defaultConfig {
        minSdkVersion(AndroidSdk.minSdkVersion)
        targetSdkVersion(AndroidSdk.targetSdkVersion)
        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildTypes {
        getByName("debug") {
            isDebuggable = true
        }

        getByName("release") {
            isMinifyEnabled = true
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(Libraries.kotlinStdLib)
    implementation(Libraries.coreKtx)

    // Material Design
    implementation(Libraries.material)

    // Firebase crashlytics
    implementation(Libraries.crashlytics)

    // Network - Retrofit, OKHTTP
    implementation(Libraries.apollo)
    implementation(Libraries.apolloCoroutines)
    implementation(Libraries.ohttp)
    implementation(Libraries.loggingInterceptor)
    debugImplementation(Libraries.chunkDebug)
    releaseImplementation(Libraries.chunkRelease)

    // Room
    implementation(Libraries.room)
    implementation(Libraries.roomRuntime)
    kapt(Libraries.roomCompiler)

    // Glide
    implementation(Libraries.glide)
    kapt(Libraries.glideCompiler)

    // Coroutines
    implementation(Libraries.coroutines)
    implementation(Libraries.coroutinesAndroid)

    // DI - KOIN
    implementation(Libraries.koin)
    implementation(Libraries.koinViewModel)

    // debug
    implementation(Libraries.timber)

    // tests
    testImplementation(TestLibraries.jUnit)
    androidTestImplementation(TestLibraries.jUnitTest)
    testImplementation(TestLibraries.truth)
}