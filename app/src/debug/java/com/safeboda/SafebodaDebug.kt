package com.safeboda

import android.os.Build
import com.facebook.stetho.Stetho

class SafebodaDebug : Safeboda() {

    override fun onCreate() {
        super.onCreate()

        initStetho()
    }

    private fun initStetho() {
        when {
            !isRoboUnitTest() && BuildConfig.DEBUG -> {
                Stetho.initializeWithDefaults(this)
            }
        }
    }

    private fun isRoboUnitTest(): Boolean = "robolectric" == Build.FINGERPRINT
}