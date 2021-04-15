package com.a6.inversiones

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDateTime


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

/*
        data class Product(val name: String, val price: Double /*USD*/)


        fun selector(p: Product): Double = p.price

        val products = arrayOf(
            Product("iPhone 8 Plus 64G", 850.00),
            Product("iPhone 8 Plus 256G", 1100.00),
            Product("Apple iPod touch 16GB", 246.00),
            Product("Apple iPod Nano 16GB", 234.75),
            Product("iPad Pro 9.7-inch 32 GB", 474.98),
            Product("iPad Pro 9.7-inch 128G", 574.99),
            Product("Apple 42mm Smart Watch", 284.93)
        )

        //products.sortBy { selector(it) }

        products.sortBy { it.price }


 */
        val localDate = LocalDateTime.now().toLocalDate()

        val viewModel = MarketStockViewModel()

        //viewModel.getAllData()

        viewModel.calculateAllCoeficiente()


    }

    companion object {
        const val TAG = "TAGGG"
    }
}