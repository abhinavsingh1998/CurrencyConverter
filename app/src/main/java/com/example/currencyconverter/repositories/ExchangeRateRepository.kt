package com.example.currencyconverter.repositories

import androidx.lifecycle.LiveData
import com.example.currencyconverter.applications.ExchangeRateApplication
import com.example.currencyconverter.models.ExchangeRateResponse
import com.example.currencyconverter.network.ExchangeRateApiService
import com.example.currencyconverter.room.entities.ExchangeRate
import com.example.currencyconverter.room.entities.HistoryExchangeRate
import retrofit2.Response

class ExchangeRateRepository(private val apiService: ExchangeRateApiService) {

    suspend fun getLatestExchangeRates(): Response<ExchangeRateResponse> {
        return apiService.getLatestExchangeRates()
    }

    suspend fun saveLatestExchangeRatesToLocal(map: Map<String, Double>) {
        val exchangeRateDao = ExchangeRateApplication.database.exchangeRateDao()
        map.keys.let { keys ->
            for (key in keys) {
                map[key]?.let { value ->
                    val exchangeRate = ExchangeRate(key, value)
                    exchangeRateDao.insertExchangeRate(exchangeRate)
                }
            }
        }
    }

    suspend fun saveHistoryExchangeRateToLocal(historyExchangeRate: HistoryExchangeRate) {
        val historyExchangeRateDao = ExchangeRateApplication.database.historyExchangeRateDao()
        historyExchangeRateDao.insert(historyExchangeRate)
    }

    fun getAllHistoryExchangeRates(): LiveData<List<HistoryExchangeRate>> {
        val historyExchangeRateDao = ExchangeRateApplication.database.historyExchangeRateDao()
        return historyExchangeRateDao.getLatest10Entries()
    }
}