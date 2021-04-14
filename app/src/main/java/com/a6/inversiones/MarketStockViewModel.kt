package com.a6.inversiones

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a6.inversiones.MainActivity.Companion.TAG
import com.a6.inversiones.data.MarketStackRepository
import com.a6.inversiones.data.SharedPreferencesManager
import com.a6.inversiones.data.analysis.EvaluateStock
import com.a6.inversiones.data.database.StockData
import com.a6.inversiones.data.network.models.DataResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class MarketStockViewModel : ViewModel(), KoinComponent {

    private val marketStockRepository: MarketStackRepository by inject()

    private val sharedPreferencesManager: SharedPreferencesManager by inject()

    fun getDB(symbol: String) {

        viewModelScope.launch(Dispatchers.IO) {

            val db: List<StockData> = marketStockRepository.getDB(symbol)!!

            val evaluator = EvaluateStock()

            var bestResult: Double = 0.0
            var bestOscilation: Double = 0.0

            for (i in 0..100) {
                val oscilador = (10.0 + i / 10)
                val result = evaluator.testLogic(db, oscilador)
                if (result > bestResult) {
                    bestResult = result
                    bestOscilation = oscilador
                }
            }

            sharedPreferencesManager.saveOscilacion(symbol, bestOscilation)

            Log.d(TAG, "Resultado final: $bestResult  con $bestOscilation")

        }
    }


    fun getNewData(symbol: String) {
        viewModelScope.launch(Dispatchers.IO) {

            when (marketStockRepository.getNewData(symbol)) {
                is DataResult.Success -> {
                    val db = marketStockRepository.getDB(symbol)

                    db?.let {
                        val evaluator = EvaluateStock()
                        evaluator.testLogic(db, 10.0)
                    }

                }
                else -> {
                    Log.e(TAG, " No se pudo obtener los datos de la API")
                }
            }

        }
    }

}

