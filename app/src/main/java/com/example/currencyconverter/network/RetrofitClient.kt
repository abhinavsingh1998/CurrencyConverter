package com.example.currencyconverter.network

import com.example.currencyconverter.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitClient {

    fun apiService(): ExchangeRateApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL + Constants.API_KEY)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    }
}