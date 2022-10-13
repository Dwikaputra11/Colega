package com.example.colega.api

import com.example.colega.models.NewsModel
import com.example.colega.utils.Utils
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RestfulApi {

    // FILM
    @GET("top-headlines?country=id")
    fun getTopHeadLines(@Query("apiKey") apiKey : String = Utils.apiKey): Call<NewsModel>

    @GET("top-headlines?country=id")
    fun getPreferences(
        @Query("category") category: String = Utils.category_technology,
        @Query("apiKey") apiKey: String = Utils.apiKey,
    ): Call<NewsModel>


    // USER

}