package com.safeboda.core.settings

import android.content.SharedPreferences
import androidx.core.content.edit

class Settings(
    val settings: SharedPreferences
) {

    var isLoggedIn: Boolean
        get() = settings.getBoolean(IS_LOGGED_IN_KEY, false)
        set(isLoggedIn) = settings.edit {
            putBoolean(IS_LOGGED_IN_KEY, isLoggedIn)
        }

    var bearerToken: String?
        get() = settings.getString(BEARER_TOKEN_KEY, "")
        set(token) = settings.edit {
            putString(BEARER_TOKEN_KEY, token)
        }

    fun clearData() = settings.edit { clear() }

    companion object SettingsConstants {
        const val SAFEBODA_SETTINGS_NAME = "safeboda_settings"
        const val IS_LOGGED_IN_KEY = "is_logged_in"
        const val BEARER_TOKEN_KEY = "bearer_token"
    }
}