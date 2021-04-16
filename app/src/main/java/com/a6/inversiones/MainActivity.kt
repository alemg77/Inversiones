package com.a6.inversiones

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.a6.inversiones.data.SharedPreferencesManager.Companion.SYMBOLS1
import com.a6.inversiones.data.SharedPreferencesManager.Companion.SYMBOLS2
import com.a6.inversiones.data.SharedPreferencesManager.Companion.SYMBOLS3
import com.a6.inversiones.data.SharedPreferencesManager.Companion.SYMBOLS4
import java.time.LocalDateTime


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val localDate = LocalDateTime.now().toLocalDate()
        //val viewModel = MarketStockViewModel()
        //viewModel.getAllData(SYMBOLS4)

        val viewModel = EstimatorViewModel()
        viewModel.evalueteCoeficiente(SYMBOLS1 + SYMBOLS2 + SYMBOLS3 + SYMBOLS4)


    }

    companion object {
        const val TAG = "TAGGG"
    }
}