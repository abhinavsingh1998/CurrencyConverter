package com.example.currencyconverter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyconverter.databinding.ItemsHistoryBinding
import com.example.currencyconverter.room.entities.HistoryExchangeRate
import java.text.DecimalFormat

class ConvertedHistoryAdapter(
    val list:  List<HistoryExchangeRate>):
    RecyclerView.Adapter<ConvertedHistoryAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemsHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[holder.adapterPosition]
        holder.binding.targetCurrency.text = item.targetCurrencyCode
        holder.binding.baseCurrency.text = item.sourceCurrencyCode
        holder.binding.targetAmmount.text = limitDecimalPlaces(item.targetCurrencyValue, 3)
        holder.binding.baseAmmount.text = item.sourceCurrencyValue.toString()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(val binding: ItemsHistoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun limitDecimalPlaces(value: Double, decimalPlaces: Int): String {
        val decimalFormat = DecimalFormat("#.${"#".repeat(decimalPlaces)}")
        return decimalFormat.format(value)
    }
}