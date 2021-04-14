package com.a6.inversiones.data

import com.a6.inversiones.data.database.StockData

class MockData {

    companion object {
        fun getData(): ArrayList<StockData> {
            val data = ArrayList<StockData>()
            data.add(StockData("9", 5.0, "M"))
            data.add(StockData("8", 5.6, "M"))
            data.add(StockData("7", 5.5, "M"))
            data.add(StockData("6", 4.8, "M"))
            data.add(StockData("5", 4.5, "M"))
            data.add(StockData("4", 4.3, "M"))
            data.add(StockData("3", 5.3, "M"))
            data.add(StockData("2", 3.1, "M"))
            data.add(StockData("1", 1.9, "M"))
            return data
        }
    }
}