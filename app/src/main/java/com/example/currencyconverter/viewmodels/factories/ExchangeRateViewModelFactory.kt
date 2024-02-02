package com.example.currencyconverter.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.currencyconverter.repositories.ExchangeRateRepository
import com.example.currencyconverter.viewmodels.ExchangeRateViewModel

class ExchangeRateViewModelFactory(private val repository: ExchangeRateRepository):
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ExchangeRateViewModel::class.java)) {
            return ExchangeRateViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}