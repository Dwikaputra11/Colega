package com.example.colega.api

import com.bumptech.glide.util.Util
import com.example.colega.models.NewsModel
import com.example.colega.utils.Utils
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RestfulApi {

    @GET("top-headlines?country=id")
    fun getTopHeadLines(@Query("apiKey") apiKey : String = Utils.apiKey): Call<NewsModel>

//    @GET("top-headlines?")
//    @GET("top-headlines?country=id&apiKey=c8d495d5e49a4927b6dfc589d3602fbc&category=technology")
    @GET("top-headlines?country=id")
    fun getPreferences(
        @Query("category") category: String = Utils.category_technology,
        @Query("apiKey") apiKey: String = Utils.apiKey,
    ): Call<NewsModel>

}