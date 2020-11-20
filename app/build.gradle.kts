plugins {
    id(BuildPlugins.androidApplication)
    id(BuildPlugins.kotlinAndroid)
    id(BuildPlugins.kotlinAndroidExtensions)
    id(BuildPlugins.ktlintPlugin)
    id(BuildPlugins.kapt)
    id(BuildPlugins.jacocoAndroid)
}

jacoco {
    toolVersion = Versions.jacoco
}

android {

    compileSdkVersion(AndroidSdk.compileSdkVersion)
    buildToolsVersion("30.0.2")

    android.buildFeatures.dataBinding = true
    android.buildFeatures.viewBinding = true

    defaultConfig {
        applicationId = "com.safeboda"
        minSdkVersion(AndroidSdk.minSdkVersion)
        targetSdkVersion(AndroidSdk.targetSdkVersion)
        versionCode = AndroidSdk.versionCode
        versionName = AndroidSdk.versionName
        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "com.safeboda.runner.MockTestRunner"
    }

//    testOptions {
// //        execution = "androidx_test_orchestrator"
//        animationsDisabled = true
//        unitTests.apply {
//            isReturnDefaultValues = true
//            isIncludeAndroidResources = true
//        }
//    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    signingConfigs {
        getByName("debug") {
            storeFile = file("../keystore/debug.keystore")
            keyAlias = "androiddebugkey"
            keyPassword = "android"
            storePassword = "android"
        }
    }

    buildTypes {
        getByName("debug") {
            isDebuggable = true
            versionNameSuffix = " - debug"
            applicationIdSuffix = ".debug"
            signingConfig = signingConfigs.getByName("debug")
        }

        getByName("release") {
            isShrinkResources = true
            isMinifyEnabled = true
        }
    }
}

kapt {
    arguments {
        arg("room.incremental", "true")
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(project(BuildModules.coreModule))

    implementation(Libraries.kotlinStdLib)
    implementation(Libraries.coreKtx)

    // Material and AndroidX
    implementation(Libraries.constraintLayout)
    implementation(Libraries.appCompat)
    implementation(Libraries.swiperefreshlayout)
    implementation(Libraries.material)

    // Firebase crashlytics
    implementation(Libraries.crashlytics)

    // Room
    implementation(Libraries.room)
    implementation(Libraries.roomRuntime)
    kapt(Libraries.roomCompiler)

    // Coroutines
    implementation(Libraries.coroutines)
    implementation(Libraries.coroutinesAndroid)

    // DI - KOIN
    implementation(Libraries.koin)
    implementation(Libraries.koinViewModel)

    // Lifecycle
    implementation(Libraries.viewModel)
    implementation(Libraries.livedata)
    implementation(Libraries.lifecycle)
    implementation(Libraries.viewModelSavedState)

    // Debug - for debug builds only
    implementation(Libraries.timber)
    debugImplementation(Libraries.leakCanary)
    debugImplementation(Libraries.stetho)

    // UI Tests
    androidTestImplementation(TestLibraries.espresso)
    androidTestImplementation(TestLibraries.kakao)

    // Instrumentation Tests
    androidTestImplementation(TestLibraries.runner)
    androidTestImplementation(TestLibraries.rules)
    androidTestImplementation(TestLibraries.koinTest)
    androidTestImplementation(TestLibraries.androidXJUnit)
    androidTestImplementation(TestLibraries.androidXTestCore)
    androidTestImplementation(TestLibraries.androidMockK)

    // Unit Tests
    testImplementation(TestLibraries.jUnit)
    testImplementation(TestLibraries.roomTest)
    testImplementation(TestLibraries.koinTest)
    testImplementation(TestLibraries.mockK)
    testImplementation(TestLibraries.roboelectric)
    testImplementation(TestLibraries.truth)
    testImplementation(TestLibraries.runner)
    testImplementation(TestLibraries.androidXJUnit)
    testImplementation(TestLibraries.coroutinesTest)
    testImplementation(TestLibraries.archComponentTest)
    testImplementation(TestLibraries.liveDataTesting)
}