package com.example.currencyconverter.room.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.currencyconverter.room.entities.HistoryExchangeRate

@Dao
interface HistoryExchangeRateDao {

    @Query("SELECT * FROM history_exchange_rates ORDER BY id DESC LIMIT 10")
    fun getLatest10Entries(): LiveData<List<HistoryExchangeRate>>

    @Insert
    suspend fun insert(historyExchangeRate: HistoryExchangeRate)
}