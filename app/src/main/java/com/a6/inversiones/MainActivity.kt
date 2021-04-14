package com.a6.inversiones

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.a6.inversiones.data.MockData
import com.a6.inversiones.data.analysis.EvaluateStock
import java.time.LocalDateTime


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val data = MockData.getData()

        val evaluator = EvaluateStock()

        Log.d(TAG, "Data: $data")

        var money = 100.0
        var valueLastBuy = 0.0
        var stock = 0.0

        for (i in 0..data.size - 3) {
            val k = data.size - 3 - i
            val subData = data.subList(k, data.size)


            if (money > 0) {
                if (evaluator.evaluateBuy(subData)) {
                    Log.d(TAG, "Compramos!!")
                    stock = (money * CONSTANTE_COMISION) / subData[0].value
                    money = 0.0
                    valueLastBuy = subData[0].value
                }
            } else {
                if (evaluator.evaluateRetire(subData, valueLastBuy)) {
                    Log.d(TAG, "Vendemos!!!")
                    money = stock * subData[0].value * CONSTANTE_COMISION
                    stock = 0.0
                }
            }

            val cap = stock * subData[0].value * CONSTANTE_COMISION

            Log.d(
                TAG,
                "${subData[0].date}: money = $money , stock = $stock , capitalizacion = $cap "
            )

        }

        val localDate = LocalDateTime.now().toLocalDate()

        val viewModel = MarketStockViewModel()

        // viewModel.getEndOfDay("AAPL,KO,JNJ,MELI")
        //viewModel.getDB("KO")

    }

    companion object {
        const val TAG = "TAGGG"
        const val CONSTANTE_COMISION = 0.98
    }
}