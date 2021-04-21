package com.a6.inversiones

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.io.*
import java.time.LocalDateTime


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (!checkPermission()) {
            askForPermission()
        }

        val localDate = LocalDateTime.now().toLocalDate()
    }

    private fun checkPermission(): Boolean {
        return (applicationContext?.let {
            ContextCompat.checkSelfPermission(
                it,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            )
        } == PackageManager.PERMISSION_GRANTED)
    }

    private fun askForPermission() {
        requestPermissions(
            arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
            PERMISSION_REQUEST_CODE
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty()) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "Me dieron permiso :)")
                        //
                    } else {
                        Log.d(TAG, "No me dieron permiso :(")
                        //
                    }
                }
            }
        }
    }

    fun testArchivo() {
        val filename = "archivo.cvs"
        val appSpecificExternalDir = File(getExternalFilesDir(null), filename)


        val fileOutputStream = FileOutputStream(appSpecificExternalDir)
        val texto = "12345678910"
        fileOutputStream.write(texto.toByteArray())
        fileOutputStream.close()


        val fileInputStream = FileInputStream(appSpecificExternalDir)
        val inputStreamReader = InputStreamReader(fileInputStream)
        val bufferedReader = BufferedReader(inputStreamReader)
        val line = bufferedReader.readLine()
        Log.d(TAG, line)
        fileInputStream.close()
    }

    companion object {
        const val TAG = "TAGGG"
        const val PERMISSION_REQUEST_CODE = 99

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
        const val Astrazeneca = "AZN"
        const val Intel = "INTC"
        const val Qualcomm = "QCOM"
        const val Taiwan_Semiconductor = "TSM"


        val SYMBOLS_CEDEAR = listOf(
            SYMBOL_Jpmorgan_Chase,
            SYMBOL_United_States_Steel,
            Petroleo_Brasileiro,
            APLE,
            Walmart,
            Nvidia,
            Microsoft,
            ExxonMobil,
            AT_T,
            BARRIC_GOLD,
            COCA_COLA,
            JOHNSON_AND_JOHNSON,
            Intel,
            Qualcomm,
            Nike,
            Bank_of_America,
            Nike,
            Harmony_Gold,
            Vale,
            General_Electric,
            Caterpillar,
            Pfizer,
            Mastercard_Inc,
            Astrazeneca,
            Taiwan_Semiconductor,
            "UNH",
            "AMZN",
            "C",
            "V",
            //"DISN",
            "WFC",
            "PG",
        )


        val SYMBOLS1 = listOf(
            "BBD",
            "AUY",
            "ABBV",
            "RIO",
            "DE",
            "ITUB",
            "AIG",
            "SONY",
            "AXP",
            "CAT",
            "MCD",
            "AVGO",
            "GGB",
            "ORCL",
            "AEM",
            "TMO",
            "LYG",
            "SBUX",
            "UNP",
            "HLT",
            "WDAY",
            "ABNB",
            "AMAT",
            "AYX",
            "LRCX",
            "QRVO"
        )

        val SYMBOLS2 = listOf(
            "CL",
            "CVX",
            "EBAY",
            "GSK",
            "FDX",
            "MMM",
            "PAAS",
            "BMY",
            "SAP",
            "FCX",
            "FCX",
            "RTX",
            "COST",
            "CAH",
            "SLB",
            "SAN",
            "NEM",
            "TOT",
            "AMGN",
            "BHP",
            "HMC",
            "WDAY",
            "ABNB",
            "AMAT",
            "AYX",
            "LRCX",
            "QRVO"
        )

        val SYMBOLS_CHINA = listOf(
            "BABA",
            "BIDU",
            "JD",
            "VIV",
            "TEN",
        )

        val SYMBOLS_ENDEUDADOS_MAS_100_PORCIENTO = listOf(
            "GILD",
        )

        val SYMBOLS_WITHOUT_DIVIDEND = listOf(
            "BIOX",
            "ZM",
            "ADGO",
            "DIS",
            "PYPL",
            "NFLX",
            "FB",
            "AMD",
            "DVA",
            "ETSY",
            "BA",
            "ADBE",
            "GOOGL",
            "SHOP",
            "SQ",
            "BIIB",
            "TRIP",
            "SNAP",
            "TSLA",
            "DOCU",
            "MELI",
            "CX",
            "SPOT",
        )

        val SYMBOLS_NO_INVERTIR = listOf(
            "CS",
            "SNOW"
        )

        val ALL_SYMBOLS = SYMBOLS_CEDEAR +
                SYMBOLS2 +
                SYMBOLS1 +
                SYMBOLS_WITHOUT_DIVIDEND +
                SYMBOLS_CHINA +
                SYMBOLS_NO_INVERTIR +
                SYMBOLS_ENDEUDADOS_MAS_100_PORCIENTO

    }

}