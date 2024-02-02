package com.example.currencyconverter.utils

import android.content.Context
import android.content.SharedPreferences

object PreferencesUtil {

    private const val PREF_NAME = "ExchangeRatePreferences"
    private lateinit var sharedPreferences: SharedPreferences

    const val NEXT_UPDATE_TIME = "NEXT_UPDATE_TIME"

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun getLong(key: String, defaultValue: Long): Long {
        return sharedPreferences.getLong(key, defaultValue) ?: defaultValue
    }

    fun putLong(key: String, value: Long) {
        sharedPreferences.edit().putLong(key, value).apply()
    }

}