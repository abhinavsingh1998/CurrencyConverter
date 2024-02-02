package com.example.currencyconverter.models

import com.google.gson.annotations.SerializedName

data class ExchangeRateResponse (
    @SerializedName("result") val result: String?,
    @SerializedName("base_code") val baseCode: String?,
    @SerializedName("conversion_rates") val conversionRates: Map<String, Double>?,
    @SerializedName("time_last_update_unix") val timeLastUpdate: Int?,
    @SerializedName("time_next_update_unix") val timeNextUpdate: Int?,
)