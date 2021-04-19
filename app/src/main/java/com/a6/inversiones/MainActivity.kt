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
        //val viewModel = MarketStockViewModel()

        //viewModel.getNewStockValue(ALL_SYMBOLS, 4)

        //viewModel.getNewDividends(SYMBOLS)

        //viewModel.getStockValue("AAPL")

        val viewModel = EstimatorViewModel()
        //viewModel.evalueteCoeficiente(ALL_SYMBOLS)
        viewModel.evaluateBuy(ALL_WITH_DIVIDEND, 0.20)


        Log.d(TAG, "Fin del onCreate en MainActivity")
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


        val SYMBOLS1 = listOf(
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
            Taiwan_Semiconductor
        )

        val SYMBOLS2 = listOf(
            "UNH",
            "C",
            "BBD",
            "V",
            "WFC",
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
            "PG",
            "AVGO",
            "GGB",
            "ORCL",
            "AEM",
            "TMO",
            "LYG",
            "VIV",
            "SBUX",
            "UNP",
            "HLT",
        )

        val SYMBOLS3 = listOf(
            "CL",
            "CVX",
            "EBAY",
            "GSK",
            "FDX",
            "CS",
            "MMM",
            "PAAS",
            "BMY",
            "SAP",
            "FCX",
            "SAP",
            "FCX",
            "RTX",
            "COST",
            "CAH",
            "GILD",
            "SLB",
            "SAN",
            "NEM",
            "TOT",
            "AMGN",
            "BHP",
            "HMC",
        )



        val SYMBOLS_WITHOUT_DIVIDEND = listOf(
            "BIOX",
            "ZM",
            "ADGO",
            "BIDU",
            "DIS",
            "PYPL",
            "NFLX",
            "FB",
            "AMD",
            "DVA",
            "JD",
            "ETSY",
            "BA",
            "TEN",
            "ADBE",
            "GOOGL",
            "SHOP",
            "SQ",
            "BIIB",
            "TRIP",
            "SNOW",
            "AMZN",
            "SNAP",
            "TSLA",
            "DOCU",
            "BABA",
            "MELI",
            "CX",
            "SPOT"
        )

        val ALL_WITH_DIVIDEND = SYMBOLS1 + SYMBOLS2 + SYMBOLS3
        val ALL_SYMBOLS = SYMBOLS1 + SYMBOLS2 + SYMBOLS3 + SYMBOLS_WITHOUT_DIVIDEND
    }

}