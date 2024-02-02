package com.example.currencyconverter.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.currencyconverter.room.daos.ExchangeRateDao
import com.example.currencyconverter.room.daos.HistoryExchangeRateDao
import com.example.currencyconverter.room.entities.ExchangeRate
import com.example.currencyconverter.room.entities.HistoryExchangeRate

@Database(entities = [ExchangeRate::class, HistoryExchangeRate::class], version = 1)
abstract class ExchangeRateDatabase : RoomDatabase() {
    abstract fun exchangeRateDao(): ExchangeRateDao
    abstract fun historyExchangeRateDao(): HistoryExchangeRateDao
}