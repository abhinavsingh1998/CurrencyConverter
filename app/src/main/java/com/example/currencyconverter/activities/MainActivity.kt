package com.example.currencyconverter.activities

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.currencyconverter.R
import com.example.currencyconverter.databinding.ActivityMainBinding
import com.example.currencyconverter.fragments.CurrencyConverterFragment
import com.example.currencyconverter.network.RetrofitClient
import com.example.currencyconverter.repositories.ExchangeRateRepository
import com.example.currencyconverter.viewmodels.ExchangeRateViewModel
import com.example.currencyconverter.viewmodels.factories.ExchangeRateViewModelFactory

class MainActivity : BaseActivity() {

    private lateinit var activityBinding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)

        attachFragment()
    }


    private fun attachFragment(){
        val myFragment = CurrencyConverterFragment()

        supportFragmentManager.beginTransaction()
            .replace(R.id.myFragment, myFragment)
            .commit()
    }

}