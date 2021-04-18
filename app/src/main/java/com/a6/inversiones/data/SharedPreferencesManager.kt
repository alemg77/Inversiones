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

        const val SYMBOL_Jpmorgan_Chase = "JPM"
        const val SYMBOL_United_States_Steel = "X"
        const val Petroleo_Brasileiro = "PBR"
        const val APLE = "AAPL"
        const val Walmart = "WMT"
        const val Nvidia = "NVDA"
        const val Microsoft = "MSFT"
        const val ExxonMobil = "XOM"
        const val AT_T = "T"
        const val BARRIC_GOLD = "GOLD"
        const val COCA_COLA = "KO"
        const val JOHNSON_AND_JOHNSON = "JNJ"
        const val Nike = "NKE"
        const val Bank_of_America = "BAC"
        const val Harmony_Gold = "HMY"
        const val Vale = "VALE"
        const val General_Electric = "GE"
        const val Caterpillar = "CAT"
        const val Pfizer = "PFE"
        const val Mastercard_Inc = "MA"
        const val Netflix = "NFLX"
        const val Astrazeneca = "AZN"
        const val Intel = "INTC"
        const val Qualcomm = "QCOM"
        const val Taiwan_Semiconductor = "TSM"
        const val Snap_Inc = "SNAP"

        val SYMBOLS1 = listOf(
            SYMBOL_Jpmorgan_Chase, SYMBOL_United_States_Steel,
            Petroleo_Brasileiro, APLE, Walmart, Nvidia, Microsoft, ExxonMobil,
            AT_T, BARRIC_GOLD, COCA_COLA, JOHNSON_AND_JOHNSON, Intel, Qualcomm,
            Nike, Bank_of_America, Nike, Harmony_Gold,
            Vale, General_Electric, Caterpillar, Pfizer, Mastercard_Inc,
            Netflix, Astrazeneca, Taiwan_Semiconductor, Snap_Inc
        )

        val SYMBOLS3 = listOf(
            "UNH",
            "C",
            "BBD",
            "V",
            "BA",
            "WFC",
            "AUY",
            "ABBV",
            "DIS",
            "RIO",
            "DE",
            "JD",
            "SPOT",
            "ITUB",
            "AIG",
            "SONY",
            "AXP",
            "CAT",
            "ADGO",
            "MCD",
            "PG",
            "AVGO",
            "GGB",
            "AMD",
            "FB",
            "MELI",
            "SHOP",
        )

        val SYMBOLS4 = listOf(
            "CL",
            "ADBE",
            "CVX",
            "EBAY",
            "ETSY",
            "GSK",
            "FDX",
            "CS",
            "MMM",
            "PAAS",
            "BMY",
            "TRIP",
            "SAP",
            "FCX",
            "SAP",
            "FCX",
            "RTX",
            "CX",
            "COST",
            "CAH",
            "GILD",
            "DOCU",
            "SLB",
            "SAN",
            "NEM",
            "TOT",
            "AMGN",
            "BHP",
            "HMC",
            "UNP",
            "SBUX",
            "VIV",
            "ORCL",
            "BIIB",
            "AEM",
            "TMO",
            "LYG"
        )

        val SYMBOLS = SYMBOLS1 + SYMBOLS3 + SYMBOLS4

        val SYMBOLS_WITHOUT_DIVIDEND = listOf(
            "BIOX",
            "ZM",
            "BIDU",
            "PYPL",
            "TEN",
            "GOOGL",
            "SQ",
            "DESP",
            "SNOW",
            "FSLR",
            "TWTR",
            "AMZN",
            "TSLA",
            "BABA",
            "SPOT"
        )

        private const val PREFERENCES_FILE = "secret_shared_prefs"
    }
}