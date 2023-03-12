package com.akguptaboy.crypto.Api

import com.akguptaboy.crypto.Models.MarketData
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInterface {

    @GET("data-api/v3/cryptocurrency/listing?start=1&limit=500")
    suspend fun getMarketData() : Response<MarketData>


}