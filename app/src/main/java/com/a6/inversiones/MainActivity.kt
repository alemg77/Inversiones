package com.a6.inversiones

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.a6.inversiones.data.models.AnalisisStockValue
import com.a6.inversiones.data.models.DataFileStockCSV
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

        val viewModel = EstimatorViewModel()

        try {
            val data = readArchivoStockCSV("aapl.csv", "AAPL")

            val evalueteCoeficiente = viewModel.buscarCoeficiente(data)

            Log.d(TAG, evalueteCoeficiente.toString())
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
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

    fun readArchivoStockCSV(filename: String, symbol: String): MutableList<AnalisisStockValue> {
        val appSpecificExternalDir = File(getExternalFilesDir(null), filename)
        val fileInputStream = FileInputStream(appSpecificExternalDir)
        val inputStreamReader = InputStreamReader(fileInputStream)
        val bufferedReader = BufferedReader(inputStreamReader)

        if (bufferedReader.readLine() != "Date,High,Low,Open,Close,Volume,Adj Close") {
            Log.e(TAG, "No era un archivo de Stock CSV")
        }

        val dataFile = mutableListOf<DataFileStockCSV>()

        var line = bufferedReader.readLine()
        while (!line.isNullOrEmpty()) {
            //Log.d(TAG, line)
            val date = line.substringBefore(",")
            line = line.substringAfter(",")
            val high = line.substringBefore(",").toDouble()
            line = line.substringAfter(",")
            val low = line.substringBefore(",").toDouble()
            line = line.substringAfter(",")
            val open = line.substringBefore(",").toDouble()
            line = line.substringAfter(",")
            val close = line.substringBefore(",").toDouble()
            line = line.substringAfter(",")
            val volume = line.substringBefore(",").toDouble()
            line = line.substringAfter(",")
            val adj = line.substringBefore(",").toDouble()
            dataFile.add(DataFileStockCSV(date, high, low, open, close, volume, adj))
            line = bufferedReader.readLine()
        }

        val data = mutableListOf<AnalisisStockValue>()

        for (i in 0 until dataFile.size) {
            val d = dataFile[dataFile.size - 1 - i]
            val stockValue = AnalisisStockValue(d.date, d.close, symbol)
            data.add(stockValue)
        }
        fileInputStream.close()
        return data
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
        const val Petroleo_Brasileiro = "PBR"     // 31B
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
        const val Taiwan_Semiconductor = "TSM"  // 615B
        const val Berkshire_Hathaway = "BRK.B"  //
        const val VolksWagen = "VOW3.DE"        // 115B
        const val Samsung = "SMAN.L"            // 444B
        const val Google = "GOOG"               // 1560B

        // Companias con mas de 200B de capital de mercado
        val SYMBOLS_BIGGEST = listOf(
            APLE,                   // 2254B
            Microsoft,              // 1969B
            "AMZN",                 // 1684B
            Taiwan_Semiconductor,   // 615B
            SYMBOL_Jpmorgan_Chase,  // 455B
            Walmart,                //
            Nvidia,                 //
            JOHNSON_AND_JOHNSON,    //
            "V",                    // 491B
            "UNH",                  // 378B
            Mastercard_Inc,         // 384B
            Bank_of_America,        // 336B
            "PG",                   // 327B
            Intel,                  // 241B
            COCA_COLA,              // 234B
            AT_T,                   // 224B
            Pfizer,                 // 215B
            Nike,                   // 205B
        )

        // Companias de 50 a 200B
        val SYMBOLS_BIG = listOf(
            Qualcomm,
            ExxonMobil,
            "WFC",              // 181B
            "C",                // 148B
            Caterpillar,        // 120B
            General_Electric,
            Astrazeneca,
        )

        val SYMBOLS = listOf(
            BARRIC_GOLD,
            Petroleo_Brasileiro,
            "BBD",
            Harmony_Gold,
            "AUY",
            "ABBV",
            "RIO",
            Vale,
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
            "LRCX",
            "QRVO",
            "CL",
            "CVX",
            "EBAY",
            "GSK",
            "FDX",
            "MMM",
            "PAAS",
            "BMY",
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
            "LRCX",
            "QRVO",
            "GILD",     // +100% de deuda
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
            "DOCU",
            "MELI",
            "SPOT",
        )

        const val CHINA_CONSTRUCTION_BANK = "0939.HK" // 624B

        val SYMBOLS_CHINA = listOf(
            "BABA",
            "BIDU",
            "JD",
            "VIV",
            "TEN",
        )

        val SYMBOLS_NO_INVERTIR = listOf(
            "CS",
            "SNOW"
        )

        val SYMBOLS_EMPRESAS_QUE_DESPLOMAN = listOf(
            "BIIB",
            "AYX",
            "SAP",
        )

        // Estas empresas rindieron muchisimo
        val SYMBOLS_TRAMPA = listOf(
            "BIOX",
            "AMAT",
            "CX",
            "TRIP",
            "SNAP",
            "ZM",
            "X",
            "FCX",
            "TSLA",
        )

        // "ADGO"

        val SYMBOLS_ETORO = SYMBOLS_BIG +
                SYMBOLS_BIGGEST +
                SYMBOLS

        val ALL_SYMBOLS = SYMBOLS_ETORO +
                SYMBOLS_CHINA +
                SYMBOLS_TRAMPA +
                SYMBOLS_NO_INVERTIR +
                SYMBOLS_EMPRESAS_QUE_DESPLOMAN


    }

}