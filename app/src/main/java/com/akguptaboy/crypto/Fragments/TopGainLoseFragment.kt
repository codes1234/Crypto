package com.akguptaboy.crypto.Fragments

import android.os.Bundle
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
import com.akguptaboy.crypto.databinding.FragmentTopGainLoseBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Collections


class TopGainLoseFragment : Fragment() {

    lateinit var binding: FragmentTopGainLoseBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTopGainLoseBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMarletData()
    }

    private fun getMarletData() {
        val position = requireArguments().getInt("position")
        lifecycleScope.launch(Dispatchers.IO) {
            val res =
                RetrofitInstance.getInstance().create(ApiInterface::class.java).getMarketData()

            if (res.body() != null) {
                withContext(Dispatchers.Main) {
                    val dataItem = res.body()!!.data.cryptoCurrencyList
                    Collections.sort(dataItem) { o1, o2 ->
                        (o2.quotes[0].percentChange24h.toInt())
                            .compareTo(o1.quotes[0].percentChange24h.toInt())
                    }
                    binding.spinKitView.visibility = GONE
                    val list = ArrayList<CryptoCurrency>()
                    if (position == 0) {
                        list.clear()
                        for (i in 0..9) {
                            list.add(dataItem[i])
                        }
                        binding.topGainLoseRecyclerView.adapter =
                            MarketAdapter(requireContext(), list, "home")

                    } else {
                        //for lose sort value
                        list.clear()
                        for (i in 0..9) {
                            list.add(dataItem[dataItem.size - 1 - i])
                        }
                        binding.topGainLoseRecyclerView.adapter =
                            MarketAdapter(requireContext(), list, "home")

                    }
                }
            }
        }
    }

}