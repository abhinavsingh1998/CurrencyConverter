package com.example.currencyconverter.applications

import android.app.Application
import androidx.room.Room
import com.example.currencyconverter.room.database.ExchangeRateDatabase
import com.example.currencyconverter.utils.PreferencesUtil

class ExchangeRateApplication: Application() {

    companion object {
        lateinit var database: ExchangeRateDatabase
    }

    override fun onCreate() {
        super.onCreate()
        PreferencesUtil.init(this)
        database = Room.databaseBuilder(applicationContext,
            ExchangeRateDatabase::class.java,
            "exchange_rate_db")
            .build()
    }
}