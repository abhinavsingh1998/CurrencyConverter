package com.example.currencyconverter.room.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.currencyconverter.room.entities.ExchangeRate

@Dao
interface ExchangeRateDao {

    @Query("SELECT * FROM exchange_rates")
    suspend fun getAllExchangeRates(): List<ExchangeRate>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExchangeRate(rate: ExchangeRate)

    @Query("SELECT * FROM exchange_rates WHERE currency_code = :currencyCode")
    fun getExchangeRateByCurrencyCode(currencyCode: String): ExchangeRate?
}