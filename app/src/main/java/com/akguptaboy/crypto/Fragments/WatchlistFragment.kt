package com.akguptaboy.crypto.Fragments

import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.createBitmap
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.akguptaboy.crypto.Adapters.MarketAdapter
import com.akguptaboy.crypto.Api.ApiInterface
import com.akguptaboy.crypto.Api.RetrofitInstance
import com.akguptaboy.crypto.Models.CryptoCurrency
import com.akguptaboy.crypto.R
import com.akguptaboy.crypto.databinding.FragmentWatchlistBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.reflect.Type


class WatchlistFragment : Fragment() {


    lateinit var binding: FragmentWatchlistBinding
    private lateinit var list: List<CryptoCurrency>
    private lateinit var adapter: MarketAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWatchlistBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        var arraylist = ArrayList<Int>()
        val sharedPrefs = requireActivity().getSharedPreferences("ssp", Context.MODE_PRIVATE)
        val json1 = sharedPrefs.getString("id", "")
        val gson = Gson()
        if (json1 != "") {
            val type: Type = object : TypeToken<ArrayList<Int?>?>() {}.type
            arraylist = gson.fromJson(json1, type)
        }
//        else if (arraylist.size == 0) {
//
//        }
//        Log.d("jh@4354", "onViewCreated: ${arraylist.size}")







        list = listOf()
        adapter = MarketAdapter(requireContext(), list, "watchlist")
        binding.watchlistRecyclerView.adapter = adapter

        lifecycleScope.launch(Dispatchers.IO) {

            if (arraylist.size != 0) {
                val res =
                    RetrofitInstance.getInstance().create(ApiInterface::class.java).getMarketData()
                if (res.body() != null) {
                    withContext(Dispatchers.Main) {

                        var newlist: ArrayList<CryptoCurrency> = ArrayList()

                        list = res.body()!!.data.cryptoCurrencyList



                        list.forEach {
                            if (arraylist.contains(it.id)) {
                                newlist.add(it)
                            }
                        }

                        adapter.updateData(newlist)
                        binding.spinKitView.visibility = View.GONE

                    }
                }
            }else{
                lifecycleScope.launch(Dispatchers.Main){
                    binding.spinKitView.visibility = GONE
                    binding.emptyTextView.visibility = VISIBLE
                }

            }
        }
//        Toast.makeText(requireContext(),   " kk ${arrayList.size}", Toast.LENGTH_LONG).show()

//        val adapter = MarketAdapter(requireContext(),arrayList,"kjkh")
//        binding.watchlistRecyclerView.adapter = adapter
    }

}