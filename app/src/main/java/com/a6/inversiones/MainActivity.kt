package com.a6.inversiones

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.a6.inversiones.data.SharedPreferencesManager.Companion.SYMBOLS2
import java.time.LocalDateTime


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val localDate = LocalDateTime.now().toLocalDate()

        //val viewModel = MarketStockViewModel()
        //viewModel.getAllData(SYMBOLS2)

        val viewModel = EstimatorViewModel()
        viewModel.evalueteCoeficiente(SYMBOLS2)


    }

    companion object {
        const val TAG = "TAGGG"
    }
}