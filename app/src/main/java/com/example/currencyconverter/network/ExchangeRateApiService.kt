package com.example.currencyconverter.network

import com.example.currencyconverter.models.ExchangeRateResponse
import retrofit2.Response
import retrofit2.http.GET

interface ExchangeRateApiService {

    @GET("latest/USD")
    suspend fun getLatestExchangeRates(): Response<ExchangeRateResponse>
}