package com.akguptaboy.crypto.Fragments

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.akguptaboy.crypto.Models.CryptoCurrency
import com.akguptaboy.crypto.R
import com.akguptaboy.crypto.databinding.FragmentDetailBinding
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class DetailFragment : Fragment() {

    lateinit var binding: FragmentDetailBinding
    private val item: DetailFragmentArgs by navArgs()


    companion object {
        var list = arrayListOf<String>()

    }

    lateinit var sharedPrefs : SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


         sharedPrefs = requireActivity().getSharedPreferences("ssp",MODE_PRIVATE)

        binding.backStackButton.setOnClickListener {
            findNavController().popBackStack()
        }


        val data: CryptoCurrency = item.data!!
        setUpDetails(data)
        loadChart(data)
        setOnclickbutton(data)

        if(sharedPrefs.contains("id")){
            list.clear()
            val json1 = sharedPrefs.getString("id", "")
            val gson = Gson()
            val type: Type = object : TypeToken<ArrayList<String?>?>() {}.type
            val arrayList: ArrayList<String> = gson.fromJson(json1,type)
            list.addAll(arrayList)
            Log.e("TAG@122", "onViewCreated ssspppp: ${arrayList.size}    ${list.size}", )


        }


        Log.e("TAG@122", "onViewCreated sss: ${list.size}", )

        list.forEach{
            Log.e("TAG@122", "onViewCreated: $it   my id ${data.id}", )
        }

        if(list.contains(data.id.toString())){
            binding.addWatchlistButton.setImageResource(R.drawable.ic_star)
            Toast.makeText(requireContext(),   "saved", Toast.LENGTH_LONG).show()
        }

        binding.addWatchlistButton.setOnClickListener {
            if(list.contains(data.id.toString())){
                binding.addWatchlistButton.setImageResource(R.drawable.ic_star_outline)
                list.remove(data.id.toString())
                val editor: SharedPreferences.Editor = sharedPrefs.edit()
                val gson = Gson()
                val json = gson.toJson(list)
                editor.putString("id", json)
                editor.apply()
            }else{
                binding.addWatchlistButton.setImageResource(R.drawable.ic_star)
                list.add(data.id.toString())
                val editor: SharedPreferences.Editor = sharedPrefs.edit()
                val gson = Gson()
                val json = gson.toJson(list)
                editor.putString("id", json)
                editor.apply()
            }







//
        }
    }

    private fun setOnclickbutton(data: CryptoCurrency) {
        val oneMonth = binding.button
        val oneWeek = binding.button1
        val oneDay = binding.button2
        val fourHour = binding.button3
        val oneHour = binding.button4
        val fifteenMinute = binding.button5


        val clickListener = View.OnClickListener {
            when (it.id) {
                fifteenMinute.id -> loadChartData(
                    it,
                    "15",
                    item,
                    oneDay,
                    oneMonth,
                    oneWeek,
                    fourHour,
                    oneHour
                )
                oneHour.id -> loadChartData(
                    it,
                    "1H",
                    item,
                    oneDay,
                    oneMonth,
                    oneWeek,
                    fourHour,
                    fifteenMinute
                )
                fourHour.id -> loadChartData(
                    it,
                    "4H",
                    item,
                    oneDay,
                    oneMonth,
                    oneWeek,
                    fifteenMinute,
                    oneHour
                )
                oneDay.id -> loadChartData(
                    it,
                    "D",
                    item,
                    fifteenMinute,
                    oneMonth,
                    oneWeek,
                    fourHour,
                    oneHour
                )
                oneWeek.id -> loadChartData(
                    it,
                    "W",
                    item,
                    oneDay,
                    oneMonth,
                    fifteenMinute,
                    fourHour,
                    oneHour
                )
                oneMonth.id -> loadChartData(
                    it,
                    "M",
                    item,
                    oneDay,
                    fifteenMinute,
                    oneWeek,
                    fourHour,
                    oneHour
                )
            }
        }
        fifteenMinute.setOnClickListener(clickListener)
        oneHour.setOnClickListener(clickListener)
        fourHour.setOnClickListener(clickListener)
        oneDay.setOnClickListener(clickListener)
        oneWeek.setOnClickListener(clickListener)
        oneMonth.setOnClickListener(clickListener)

    }

    private fun loadChartData(
        it: View?,
        s: String,
        item: DetailFragmentArgs,
        oneDay: AppCompatButton,
        oneMonth: AppCompatButton,
        oneWeek: AppCompatButton,
        fourHour: AppCompatButton,
        oneHour: AppCompatButton
    ) {
        disableButton(oneDay, oneMonth, oneWeek, fourHour, oneHour)
        it!!.setBackgroundResource(R.drawable.active_button)
        binding.detaillChartWebView.settings.javaScriptEnabled = true
        binding.detaillChartWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        Log.d("oneday", "loadChartData: ${item.data!!.symbol}   ${s}")
        binding.detaillChartWebView.loadUrl(
            "https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol" + item.data!!.symbol
                .toString() + "USD&interval=" + s + "&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1&timezone=Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT"
        )

    }

    private fun disableButton(
        oneDay: AppCompatButton,
        oneMonth: AppCompatButton,
        oneWeek: AppCompatButton,
        fourHour: AppCompatButton,
        oneHour: AppCompatButton
    ) {
        oneDay.setBackgroundResource(R.drawable.disable_button)
        oneMonth.setBackgroundResource(R.drawable.disable_button)
        oneWeek.setBackgroundResource(R.drawable.disable_button)
        oneHour.setBackgroundResource(R.drawable.disable_button)
        fourHour.setBackgroundResource(R.drawable.disable_button)

    }

    private fun loadChart(data: CryptoCurrency) {
        binding.detaillChartWebView.settings.javaScriptEnabled = true
        binding.detaillChartWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        binding.detaillChartWebView.loadUrl(
            "https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol" + item.data!!.symbol
                .toString() + "USD&interval=D&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1&timezone=Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT"
        )


    }

    private fun setUpDetails(data: CryptoCurrency) {
        binding.detailSymbolTextView.text = data.symbol

        Glide.with(requireContext())
            .load("https://s2.coinmarketcap.com/static/img/coins/64x64/${data.id}.png")
            .thumbnail(Glide.with(requireContext()).load(R.drawable.spinner))
            .into(binding.detailImageView)

        binding.detailPriceTextView.text = "${String.format("$%.4f", data.quotes[0].price)}"

        if (data.quotes!![0].percentChange24h > 0) {
            binding.detailChangeTextView.setTextColor(requireContext().resources.getColor(R.color.green))
            binding.detailChangeImageView.setImageResource(R.drawable.ic_caret_up)
            binding.detailChangeTextView.text =
                "+${String.format("%.02f", data.quotes[0].percentChange24h)} %"
        } else {
            binding.detailChangeTextView.setTextColor(requireContext().resources.getColor(R.color.red))
            binding.detailChangeImageView.setImageResource(R.drawable.ic_caret_down)
            binding.detailChangeTextView.text =
                "${String.format("%.02f", data.quotes[0].percentChange24h)} %"
        }

    }

    override fun onStart() {
        super.onStart()


    }

}