package com.example.currencyconverter.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.currencyconverter.utils.Constants

@Entity(tableName = Constants.TABLE_HISTORY_EXCHANGE_RATES)
data class HistoryExchangeRate (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo("source_currency_code")
    val sourceCurrencyCode: String,

    @ColumnInfo("source_currency_value")
    val sourceCurrencyValue: Double,

    @ColumnInfo("target_currency_code")
    val targetCurrencyCode: String,

    @ColumnInfo("target_currency_value")
    val targetCurrencyValue: Double,

    @ColumnInfo("conversion_time")
    val conversionTime: Long
)
