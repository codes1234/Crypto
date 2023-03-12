package com.akguptaboy.crypto.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.akguptaboy.crypto.Fragments.HomeFragmentDirections
import com.akguptaboy.crypto.Fragments.MarketFragmentDirections
import com.akguptaboy.crypto.Fragments.WatchlistFragment
import com.akguptaboy.crypto.Fragments.WatchlistFragmentDirections
import com.akguptaboy.crypto.Models.CryptoCurrency
import com.akguptaboy.crypto.R
import com.akguptaboy.crypto.databinding.CurrencyItemLayoutBinding
import com.bumptech.glide.Glide
import kotlin.reflect.typeOf

class MarketAdapter(var context: Context, var list: List<CryptoCurrency>, var type: String) :
    RecyclerView.Adapter<MarketAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : ViewHolder(itemView) {
        var binding = CurrencyItemLayoutBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.currency_item_layout, parent, false)
        )
    }

    fun updateData(dataItem: List<CryptoCurrency>) {
        list = dataItem
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]

        holder.binding.currencyNameTextView.text = item.name
        holder.binding.currencySymbolTextView.text = item.symbol
        Glide.with(context)
            .load("https://s2.coinmarketcap.com/static/img/coins/64x64/${item.id}.png")
            .thumbnail(Glide.with(context).load(R.drawable.spinner))
            .into(holder.binding.currencyImageView)

        Glide.with(context)
            .load("https://s3.coinmarketcap.com/generated/sparklines/web/7d/usd/${item.id}.png")
            .thumbnail(Glide.with(context).load(R.drawable.spinner))
            .into(holder.binding.currencyChartImageView)

        holder.binding.currencyPriceTextView.text =
            "${String.format("$%.02f", item.quotes[0].price)}"

        if (item.quotes!![0].percentChange24h > 0) {
            holder.binding.currencyChangeTextView.setTextColor(context.resources.getColor(R.color.green))
            holder.binding.currencyChangeTextView.text =
                "+${String.format("%.02f", item.quotes[0].percentChange24h)} %"
        } else {
            holder.binding.currencyChangeTextView.setTextColor(context.resources.getColor(R.color.red))
            holder.binding.currencyChangeTextView.text =
                "${String.format("%.02f", item.quotes[0].percentChange24h)} %"
        }

        holder.itemView.setOnClickListener {
            if (type == "home") {
                findNavController(it).navigate(
                    HomeFragmentDirections.actionHomeFragmentToDetailFragment(item)
                )
            } else if (type == "market") {
                findNavController(it).navigate(
                    MarketFragmentDirections.actionMarketFragmentToDetailFragment(item)
                )
            }else if (type == "watchlist") {
                findNavController(it).navigate(
                    WatchlistFragmentDirections.actionWatchlistFragmentToDetailFragment(item)
                )
            }

        }

    }

    override fun getItemCount(): Int {
        return list.size
    }



}