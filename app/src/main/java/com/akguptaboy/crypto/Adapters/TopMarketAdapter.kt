package com.akguptaboy.crypto.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.akguptaboy.crypto.Fragments.HomeFragmentDirections
import com.akguptaboy.crypto.Fragments.MarketFragmentDirections
import com.akguptaboy.crypto.Fragments.WatchlistFragmentDirections
import com.akguptaboy.crypto.Models.CryptoCurrency
import com.akguptaboy.crypto.R
import com.akguptaboy.crypto.databinding.TopCurrencyLayoutBinding
import com.bumptech.glide.Glide

class TopMarketAdapter(var context: Context, val list: List<CryptoCurrency>,var type:String) :
    RecyclerView.Adapter<TopMarketAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.top_currency_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]
        holder.binding.topCurrencyNameTextView.text=item.name
        Glide.with(context).load("https://s2.coinmarketcap.com/static/img/coins/64x64/${item.id}.png").
            thumbnail(Glide.with(context).load(R.drawable.spinner))
            .into(holder.binding.topCurrencyImageView)
        if (item.quotes!![0].percentChange24h>0){
            holder.binding.topCurrencyChangeTextView.setTextColor(context.resources.getColor(R.color.green))
            holder.binding.topCurrencyChangeTextView.text="+${String.format("%.02f",item.quotes[0].percentChange24h)} %"
        }else{
            holder.binding.topCurrencyChangeTextView.setTextColor(context.resources.getColor(R.color.red))
            holder.binding.topCurrencyChangeTextView.text="${String.format("%.02f",item.quotes[0].percentChange24h)} %"
        }
        holder.itemView.setOnClickListener {
            if (type == "home") {
                Navigation.findNavController(it).navigate(
                    HomeFragmentDirections.actionHomeFragmentToDetailFragment(item)
                )
            } else if (type == "market") {
                Navigation.findNavController(it).navigate(
                    MarketFragmentDirections.actionMarketFragmentToDetailFragment(item)
                )
            }else if (type == "watchlist") {
                Navigation.findNavController(it).navigate(
                    WatchlistFragmentDirections.actionWatchlistFragmentToDetailFragment(item)
                )
            }

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding = TopCurrencyLayoutBinding.bind(itemView)
    }
}