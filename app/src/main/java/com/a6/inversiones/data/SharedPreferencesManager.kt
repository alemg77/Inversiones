package com.a6.inversiones.data

import android.app.Application
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SharedPreferencesManager(app: Application) {

    var masterKey = MasterKey.Builder(app, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    var sharedPreferences = EncryptedSharedPreferences.create(
        app,
        PREFERENCES_FILE,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveOscilacion(key: String, value: Double) {
        saveLong(key, value.toLong())
    }


    fun getOscilacion(key: String): Double {
        return getLong(key).toDouble()
    }

    fun getLong(key: String): Long {
        return sharedPreferences.getLong(key, 0)
    }

    fun getString(name: String): String? {
        return sharedPreferences.getString(name, null)
    }

    fun saveString(value: String, name: String) {
        with(sharedPreferences.edit()) {
            putString(name, value)
            apply()
        }
    }

    fun saveLong(key: String, long: Long) {
        with(sharedPreferences.edit()) {
            putLong(key, long)
            apply()
        }
    }

    fun saveBoolean(key: String, boolean: Boolean) {
        with(sharedPreferences.edit()) {
            putBoolean(key, boolean)
            apply()
        }
    }

    fun getBoolean(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    companion object {
        private const val PREFERENCES_FILE = "secret_shared_prefs"
    }
}