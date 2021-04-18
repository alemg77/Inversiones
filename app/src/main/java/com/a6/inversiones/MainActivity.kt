package com.a6.inversiones

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.a6.inversiones.data.SharedPreferencesManager.Companion.SYMBOLS1
import java.io.*
import java.time.LocalDateTime


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val localDate = LocalDateTime.now().toLocalDate()

        if (!checkPermission()) {
            Log.d(TAG, "No tengo permiso, voy a pedirlo")
            askForPermission()
        } else {
            Log.d(TAG, " Ya tenia permiso antes de empezar")
        }


        val viewModel = MarketStockViewModel()
        //viewModel.test(SYMBOLS)
        viewModel.getAllData(SYMBOLS1)

        //val viewModel = EstimatorViewModel()
        //viewModel.evalueteCoeficiente(SYMBOLS1 + SYMBOLS2 + SYMBOLS3 + SYMBOLS4)


        Log.d(TAG, "Fin ??")
    }


    private fun readFromFile() {

        var ret = ""

        try {
            val inputStream: InputStream = openFileInput("config.txt")
            if (inputStream != null) {
                val inputStreamReader = InputStreamReader(inputStream)
                val bufferedReader = BufferedReader(inputStreamReader)
                var receiveString: String? = ""
                val stringBuilder = StringBuilder()
                while (bufferedReader.readLine().also { receiveString = it } != null) {
                    stringBuilder.append("\n").append(receiveString)
                }
                inputStream.close()
                ret = stringBuilder.toString()
            }
        } catch (e: FileNotFoundException) {
            Log.e("login activity", "File not found: " + e.toString())
        } catch (e: IOException) {
            Log.e("login activity", "Can not read file: " + e.toString())
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
    }
}