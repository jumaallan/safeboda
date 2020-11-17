package com.safeboda.core.di

import android.content.Context
import com.safeboda.core.settings.Settings
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

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
    settingsModule
)