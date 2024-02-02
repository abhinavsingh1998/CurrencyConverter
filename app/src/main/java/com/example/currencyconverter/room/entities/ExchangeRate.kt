package com.example.currencyconverter.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.currencyconverter.utils.Constants

@Entity(tableName = Constants.TABLE_NAME)
data class ExchangeRate (

    @PrimaryKey
    @ColumnInfo(name = "currency_code")
    val currencyCode: String,

    @ColumnInfo("rate")
    val rate: Double,
)