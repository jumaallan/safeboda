package com.safeboda.core.di

import android.content.Context
import com.google.gson.GsonBuilder
import com.readystatesoftware.chuck.ChuckInterceptor
import com.safeboda.core.BuildConfig
import com.safeboda.core.network.AuthInterceptor
import com.safeboda.core.settings.Settings
import com.safeboda.core.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkingModule: Module = module(override = true) {

    single {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = when (BuildConfig.BUILD_TYPE) {
            "release" -> HttpLoggingInterceptor.Level.NONE
            else -> HttpLoggingInterceptor.Level.BODY
        }

        val chuckInterceptor = ChuckInterceptor(androidContext()).showNotification(true)

        val authInterceptor = AuthInterceptor(get())

        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(chuckInterceptor)
            .addInterceptor(authInterceptor)
            .connectTimeout(10, TimeUnit.MINUTES)
            .readTimeout(10, TimeUnit.MINUTES)
            .build()
    }

    single {

        val gson = GsonBuilder()
            .serializeNulls()
            .create()

        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(get())
            .build()
    }
}

val settingsModule: Module = module {

    single {
        androidContext().getSharedPreferences(
            Settings.SAFEBODA_SETTINGS_NAME,
            Context.MODE_PRIVATE
        )
    }

    single { Settings(get()) }
}

val coreModules: List<Module> = listOf(
    networkingModule,
    settingsModule
)