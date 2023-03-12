package com.akguptaboy.crypto.Fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.akguptaboy.crypto.Adapters.MarketAdapter
import com.akguptaboy.crypto.Api.ApiInterface
import com.akguptaboy.crypto.Api.RetrofitInstance
import com.akguptaboy.crypto.Models.CryptoCurrency
import com.akguptaboy.crypto.databinding.FragmentMarketBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class MarketFragment : Fragment() {

lateinit var binding: FragmentMarketBinding
private lateinit var list: List<CryptoCurrency>
private lateinit var adapter: MarketAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMarketBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list = listOf()

        adapter = MarketAdapter(requireContext(),list,"market")
        binding.currencyRecyclerView.adapter = adapter

        lifecycleScope.launch(Dispatchers.IO){
            val res = RetrofitInstance.getInstance().create(ApiInterface::class.java).getMarketData()
            if (res.body() != null){
                withContext(Dispatchers.Main){
                    list = res.body()!!.data.cryptoCurrencyList
                    adapter.updateData(list)
                    binding.spinKitView.visibility = GONE

                }
            }
        }

        searchCoin()

    }
lateinit var searchText: String
    private fun searchCoin() {
        binding.searchEditText.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
              searchText = p0.toString().toLowerCase()
                updateRecyclerView()
            }

        })
    }

    private fun updateRecyclerView() {
        var data = ArrayList<CryptoCurrency>()
         for (item in list){
             var coinName = item.name.lowercase(Locale.getDefault())
             var coinSymble = item.symbol.lowercase(Locale.getDefault())

             if (coinName.contains(searchText) || coinSymble.contains(searchText)){
                 data.add(item)
             }
         }
        adapter.updateData(data)
    }


}