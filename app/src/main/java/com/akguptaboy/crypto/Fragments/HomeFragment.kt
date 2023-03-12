package com.akguptaboy.crypto.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.akguptaboy.crypto.Adapters.TopMarketAdapter
import com.akguptaboy.crypto.Adapters.ToplosgainPagerAdapter
import com.akguptaboy.crypto.Api.ApiInterface
import com.akguptaboy.crypto.Api.RetrofitInstance
import com.akguptaboy.crypto.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        getTopCurrencylist()
        setTablayout()
        return binding.root
    }

    private fun setTablayout() {
        val adapter = ToplosgainPagerAdapter(this)
        binding.contentViewPager.adapter = adapter
        binding.contentViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                if(position == 0){

                    binding.topGainIndicator.visibility = VISIBLE
                    binding.topLoseIndicator.visibility = GONE
                }else{
                    binding.topGainIndicator.visibility = GONE
                    binding.topLoseIndicator.visibility = VISIBLE
                }
            }
        })
        TabLayoutMediator(binding.tabLayout,binding.contentViewPager){
            tab,position ->
            var title = if (position  == 0){
                "Top Gainers"
            }else{
                "Top Losers"
            }
            tab.text = title
        }.attach()
    }


    private fun getTopCurrencylist() {
        lifecycleScope.launch(Dispatchers.IO) {
            val res = RetrofitInstance.getInstance().create(ApiInterface::class.java).getMarketData()

            withContext(Dispatchers.Main){
                binding.topCurrencyRecyclerView.adapter = TopMarketAdapter(requireContext(),res.body()!!.data.cryptoCurrencyList,"home")
            }
            Log.d("TAG@12", "getTopcurrencylist: ${res.body()!!.data.cryptoCurrencyList}")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}