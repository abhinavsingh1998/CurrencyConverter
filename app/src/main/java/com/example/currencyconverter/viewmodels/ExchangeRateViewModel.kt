package com.example.currencyconverter.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.applications.ExchangeRateApplication
import com.example.currencyconverter.models.ExchangeRateResponse
import com.example.currencyconverter.repositories.ExchangeRateRepository
import com.example.currencyconverter.room.entities.ExchangeRate
import com.example.currencyconverter.room.entities.HistoryExchangeRate
import com.example.currencyconverter.utils.PreferencesUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExchangeRateViewModel(private val repository: ExchangeRateRepository): ViewModel() {

    val sourceCurrency = MutableLiveData<ExchangeRate>()
    val targetCurrency = MutableLiveData<ExchangeRate>()
    val sourceCurrencyAmount = MutableLiveData<String>()


    private val exchangeRatesMutableLiveData = MutableLiveData<List<ExchangeRate>>()
    val exchangeRatesLiveData: LiveData<List<ExchangeRate>> by lazy {
        exchangeRatesMutableLiveData
    }

    var historyExchangeRatesLiveData: LiveData<List<HistoryExchangeRate>> = repository.getAllHistoryExchangeRates()

    fun getLatestExchangeRates() {
        val currentTime = System.currentTimeMillis()
        val nextUpdateTime = PreferencesUtil.getLong(PreferencesUtil.NEXT_UPDATE_TIME, currentTime)
        if(currentTime >= nextUpdateTime) {
            getExchangeRatesFromApi()
        } else {
            getExchangeRatesFromLocal()
        }
    }

    private fun getExchangeRatesFromLocal() {
        Log.i("Method_Called " , "getExchangeRatesFromLocal")
        viewModelScope.launch(Dispatchers.IO) {
            val exchangeRateDao = ExchangeRateApplication.database.exchangeRateDao()
            val response = exchangeRateDao.getAllExchangeRates()
            exchangeRatesMutableLiveData.postValue(response)
        }
    }

    private fun getExchangeRatesFromApi() {
        Log.i("Method_Called " , "getExchangeRatesFromApi")
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getLatestExchangeRates()
            if(response.isSuccessful) {
                val erResponse = response.body()
                erResponse?.let { er ->
                    er.timeNextUpdate?.let { timeInSec ->
                        val timeInMillis = timeInSec * 1000L
                        PreferencesUtil.putLong(PreferencesUtil.NEXT_UPDATE_TIME, timeInMillis)
                    }
                    er.conversionRates?.let { map ->
                        val exchangeRateList = convertExchangeRateMapToEntity(map)
                        exchangeRatesMutableLiveData.postValue(exchangeRateList)
                        repository.saveLatestExchangeRatesToLocal(map)
                    }
                }
            } else {
                //TODO Handle Failure response
            }
        }
    }

    private fun convertExchangeRateMapToEntity(map: Map<String, Double>): List<ExchangeRate> {
        val exchangeRateList = ArrayList<ExchangeRate>()
        map.keys.let { keys ->
            for (key in keys) {
                map[key]?.let { value ->
                    val exchangeRate = ExchangeRate(key, value)
                    exchangeRateList.add(exchangeRate)
                }
            }
        }
        return exchangeRateList
    }

    fun saveHistoryExchangeRate(historyExchangeRate: HistoryExchangeRate) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveHistoryExchangeRateToLocal(historyExchangeRate)
        }
    }
}