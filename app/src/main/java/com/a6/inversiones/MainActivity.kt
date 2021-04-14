package com.a6.inversiones

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.a6.inversiones.data.SharedPreferencesManager.Companion.Jpmorgan_Chase
import java.time.LocalDateTime


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*
        val data = MockData.getData()

        val evaluator = EvaluateStock()

        evaluator.testLogic(data)

         */

        val localDate = LocalDateTime.now().toLocalDate()

        val viewModel = MarketStockViewModel()


        //viewModel.getNewData(Jpmorgan_Chase)
        viewModel.getDB(Jpmorgan_Chase)

        //viewModel.getEndOfDay("AAPL,KO,JNJ,MELI")
        //viewModel.getDB("KO")

    }

    companion object {
        const val TAG = "TAGGG"


    }
}