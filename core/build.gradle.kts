plugins {
    id(BuildPlugins.androidLibrary)
    id(BuildPlugins.kotlinAndroid)
    id(BuildPlugins.kotlinAndroidExtensions)
    id(BuildPlugins.kapt)
    id(BuildPlugins.apollo).version(Versions.apolloVersion)
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

    val GITHUB_TOKEN: String? =
        com.android.build.gradle.internal.cxx.configure.gradleLocalProperties(rootDir)
            .getProperty("GITHUB_TOKEN")

    buildTypes {
        getByName("debug") {
            isDebuggable = true
            buildConfigField("String", "GITHUB_TOKEN", GITHUB_TOKEN.toString())
        }

        getByName("release") {
            isMinifyEnabled = true
            buildConfigField("String", "GITHUB_TOKEN", GITHUB_TOKEN.toString())
        }
    }
}

apollo {
    // instruct the compiler to generate Kotlin models
    generateKotlinModels.set(true)
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(Libraries.kotlinStdLib)
    implementation(Libraries.coreKtx)

    // Material Design
    implementation(Libraries.material)
    implementation(Libraries.swiperefreshlayout)

    // Firebase crashlytics
    implementation(Libraries.crashlytics)

    // Network - ApolloClient, OKHTTP, Chuck
    implementation(Libraries.apollo)
    implementation(Libraries.apolloCoroutines)
    implementation(Libraries.ohttp)
    implementation(Libraries.loggingInterceptor)
    debugImplementation(Libraries.chunkDebug)
    releaseImplementation(Libraries.chunkRelease)

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

    // Unit Tests
    testImplementation(TestLibraries.jUnit)
    testImplementation(TestLibraries.truth)
}