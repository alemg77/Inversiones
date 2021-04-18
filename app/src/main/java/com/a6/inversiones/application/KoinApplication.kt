package com.a6.inversiones.application

import android.app.Application
import com.a6.inversiones.MarketStockViewModel
import com.a6.inversiones.data.SharedPreferencesManager
import com.a6.inversiones.data.StockRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

class KoinApplication : Application() {

    private val myModule: Module = module {
        factory { SharedPreferencesManager(androidApplication()) }
        factory { StockRepository(androidApplication()) }
        viewModel { MarketStockViewModel() }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@KoinApplication)
            modules(myModule)
        }
    }

}

