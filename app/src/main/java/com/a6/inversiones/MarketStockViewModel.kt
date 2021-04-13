package com.a6.inversiones

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a6.inversiones.MainActivity.Companion.TAG
import com.a6.inversiones.data.MarketStackRepository
import com.a6.inversiones.data.database.StockData
import com.a6.inversiones.data.network.models.DataResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class MarketStockViewModel : ViewModel(), KoinComponent {

    private val marketStockRepository: MarketStackRepository by inject()

    fun getDB(symbol: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val db = marketStockRepository.getDB(symbol)
            Log.d(TAG, db.toString())
        }
    }

    fun getEndOfDay(symbols: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val response = marketStockRepository.getData(symbols)) {
                is DataResult.Success -> {
                    val arrayList = response.data as ArrayList<StockData>
                    Log.d(TAG, arrayList.toString())
                }
                else -> {
                    Log.e(TAG, " No se pudo obtener los datos de la API")
                }
            }
        }
    }


}