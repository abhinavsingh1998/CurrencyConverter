package com.example.currencyconverter.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyconverter.R
import com.example.currencyconverter.adapter.ConvertedHistoryAdapter
import com.example.currencyconverter.databinding.FragmentCurrencyConverterBinding
import com.example.currencyconverter.network.RetrofitClient
import com.example.currencyconverter.repositories.ExchangeRateRepository
import com.example.currencyconverter.room.entities.ExchangeRate
import com.example.currencyconverter.room.entities.HistoryExchangeRate
import com.example.currencyconverter.viewmodels.ExchangeRateViewModel
import com.example.currencyconverter.viewmodels.factories.ExchangeRateViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import kotlin.properties.Delegates

class CurrencyConverterFragment : Fragment() {

    private lateinit var binding: FragmentCurrencyConverterBinding

    private lateinit var viewModel: ExchangeRateViewModel
    private lateinit var erList: List<ExchangeRate>
    private var firstPosition:Int? = null
    private var secondPosition:Int? = null
    private var scAmount:String?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCurrencyConverterBinding.inflate(inflater, container, false)

        val repository = ExchangeRateRepository(RetrofitClient.apiService())
        viewModel = ViewModelProvider(requireActivity(),
            ExchangeRateViewModelFactory(repository))[ExchangeRateViewModel::class.java]

        addObservers()

        viewModel.getLatestExchangeRates()

        addListeners()
        calculateValue()
        swapCurrency()

        return binding.root
    }

    private fun addObservers() {
        viewModel.exchangeRatesLiveData.observe(viewLifecycleOwner, Observer{ er ->
            erList = er
            var list = ArrayList<String>()
            er.forEach { data ->
                list.add(data.currencyCode)
            }
            initializeSpinners(list)
        })

        viewModel.historyExchangeRatesLiveData.observe(viewLifecycleOwner, Observer { herList ->

        viewModel.sourceCurrency.observe(viewLifecycleOwner, Observer {
            binding.tvCurrencyFirst.text = it.currencyCode
            calculateValue()
        })

        viewModel.targetCurrency.observe(viewLifecycleOwner, Observer {
            binding.tvCurrencySecond.text = it.currencyCode
            calculateValue()
        })

        viewModel.sourceCurrencyAmount.observe(viewLifecycleOwner, Observer { scAmount ->

            this.scAmount = scAmount

            calculateValue()

        })

           initilizeAdapter(herList)
        })

    }

    private fun addListeners() {
        binding.tvFirstCurrency.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                viewModel.sourceCurrency.postValue(erList[p2])

                firstPosition = p2
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        binding.tvSecondCurrency.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                viewModel.targetCurrency.postValue(erList[p2])
                secondPosition = p2
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        binding.btnConvert.setOnClickListener {
            if (binding.etBaseCurrency.text.isEmpty()) {
                Toast.makeText(context, "Please enter a valid amount!", Toast.LENGTH_SHORT).show()
            } else {
                val sourceCurrencyCode = viewModel.sourceCurrency.value?.currencyCode!!
                val targetCurrencyCode = viewModel.targetCurrency.value?.currencyCode!!
                val historyExchangeRate = HistoryExchangeRate(
                    0,
                    sourceCurrencyCode,
                    binding.etBaseCurrency.text.toString().toDouble(),
                    targetCurrencyCode,
                    binding.etTargetCurrency.text.toString().toDouble(),
                    System.currentTimeMillis()
                )
                viewModel.saveHistoryExchangeRate(historyExchangeRate)
            }
        }

        binding.etBaseCurrency.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.sourceCurrencyAmount.postValue(p0?.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        binding.btnSwap.setOnClickListener {

            binding.tvFirstCurrency.setSelection(secondPosition!!)
            binding.tvSecondCurrency.setSelection(firstPosition!!)

        }

    }

    fun initializeSpinners(list:List<String>){

        val spinner1St = binding.tvFirstCurrency
        val spinner2nd = binding.tvSecondCurrency

        val adapter =  ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, list)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner1St.adapter = adapter
        spinner2nd.adapter = adapter

        spinner2nd.setSelection(1)
    }

    private fun swapCurrency(){

    }

    private fun calculateValue(){

        if(!scAmount.isNullOrEmpty()) {
            val scAmountDouble = scAmount?.toDouble()
            val sourceCurrencyValue = viewModel.sourceCurrency.value?.rate!!
            val targetCurrencyValue = viewModel.targetCurrency.value?.rate!!
            if(sourceCurrencyValue != 0.0) {
                val tcAmountDouble = (targetCurrencyValue / sourceCurrencyValue) * scAmountDouble!!
                binding.etTargetCurrency.text = limitDecimalPlaces(tcAmountDouble, 3)
            }
        } else {
            binding.etTargetCurrency.text = ""
        }
    }

    fun initilizeAdapter(herList: List<HistoryExchangeRate>){
        val adapter = ConvertedHistoryAdapter(herList)
        val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        binding.rvHistory.layoutManager = layoutManager
        binding.rvHistory.adapter = adapter
    }

    fun limitDecimalPlaces(value: Double, decimalPlaces: Int): String {
        val decimalFormat = DecimalFormat("#.${"#".repeat(decimalPlaces)}")
        return decimalFormat.format(value)
    }

}