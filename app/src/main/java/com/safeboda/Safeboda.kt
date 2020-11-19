/*
 * Copyright 2020 Safeboda
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.safeboda

import android.app.Application
import com.safeboda.core.di.coreModules
import com.safeboda.core.utils.CrashlyticsTree
import com.safeboda.di.appModules
import org.jetbrains.annotations.NotNull
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.error.KoinAppAlreadyStartedException
import org.koin.core.logger.Level
import org.koin.core.module.Module
import timber.log.Timber
import javax.annotation.Nullable

open class Safeboda : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin()
        initTimber()
    }

    private fun initKoin() {
        try {
            startKoin {
                androidLogger(Level.ERROR)
                androidContext(applicationContext)
                val modules = mutableListOf<Module>().apply {
                    addAll(coreModules)
                    addAll(appModules)
                }
                modules(modules)
            }
        } catch (error: KoinAppAlreadyStartedException) {
            Timber.e(error.localizedMessage)
        }
    }

    private fun initTimber() = when {
        BuildConfig.DEBUG -> {
            Timber.plant(object : Timber.DebugTree() {
                @Nullable
                override fun createStackElementTag(@NotNull element: StackTraceElement): String? {
                    return super.createStackElementTag(element) + ":" + element.lineNumber
                }
            })
        }
        else -> Timber.plant(CrashlyticsTree())
    }
}