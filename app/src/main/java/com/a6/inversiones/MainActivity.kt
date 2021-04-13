package com.a6.inversiones

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDateTime


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val localDate = LocalDateTime.now().toLocalDate()

        val viewModel = MarketStockViewModel()

        // viewModel.getEndOfDay("AAPL,KO,JNJ,MELI")
        viewModel.getDB("KO")

    }

    companion object {
        const val TAG = "TAGGG"
    }
}