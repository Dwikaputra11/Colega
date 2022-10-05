package com.example.colega.api

import com.example.colega.models.NewsEverything
import com.example.colega.utils.Utils
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RestfulApi {

    @GET("?q=tech&apiKey={apiKey}")
    fun getNewsData(@Path("apiKey") apiKey : String = Utils.apiKey): Call<NewsEverything>

}