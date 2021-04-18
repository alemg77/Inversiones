package com.a6.inversiones

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a6.inversiones.data.SharedPreferencesManager
import com.a6.inversiones.data.StockRepository
import com.a6.inversiones.data.analysis.EvaluateStock
import com.a6.inversiones.data.database.Dividend
import com.a6.inversiones.data.database.StockValue
import com.a6.inversiones.data.models.Estimador
import com.a6.inversiones.data.network.models.DataResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class MarketStockViewModel : ViewModel(), KoinComponent {

    private val stockRepository: StockRepository by inject()

    private val sharedPreferencesManager: SharedPreferencesManager by inject()

    private var coeficientes: MutableList<Estimador> = mutableListOf()


    /*
        coeficientes.sortBy { it.valuex100 }
     */

    private val evaluate = EvaluateStock(
        EvaluateStock.MIN_DAY_ANALISIS,
        EvaluateStock.CONSTANTE_COMISION,
        EvaluateStock.COEFICIENTE_NO_VENDER_SI_VOY_GANADO,
        EvaluateStock.COEFIENTE_NO_COMPRAR_CUANDO_CAE
    )


    fun getNewDividends(symbol: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            for (i in symbol.indices) {
                when (val result = stockRepository.getDivident(symbol[i])) {
                    is DataResult.Success -> {
                        val dividend = result.data as Dividend
                        Log.d(TAG, dividend.toString())
                    }
                    else -> {

                    }
                }
            }
        }
    }

    fun getDividends() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = stockRepository.getAllDivident()) {
                is DataResult.Success -> {
                    val dividend = result.data as List<Dividend>
                    Log.d(TAG, dividend.toString())
                }
                else -> {

                }
            }

        }
    }

    fun getNewStockValue(symbols: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            val arrayList = ArrayList<String>(symbols)
            stockRepository.getNewStockValue(arrayList)
        }
    }

    fun getStockValue(symbol: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val data = stockRepository.getStockValue(symbol)
            Log.d(TAG, data.toString())
        }
    }

    fun buscarMinimos(symbol: List<String>) {
        viewModelScope.launch {
            for (i in symbol.indices) {
                val db: List<StockValue> = stockRepository.getStockValue(symbol[i])!!
                val maxValue = evaluate.maxValue(db)
                val actual = 100 * db[0].value / maxValue
                Log.d(TAG, " ${symbol[i]} esta al $actual%")
            }
        }
    }



    companion object {
        const val TAG = "TAGG VIEW MODEL"
    }

}


