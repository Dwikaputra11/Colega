package com.example.colega.api

import com.example.colega.data.DataUser
import com.example.colega.models.news.NewsModel
import com.example.colega.models.user.UserResponseItem
import com.example.colega.utils.Utils
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
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
    @GET("users")
    fun getAllUsers(): Call<List<UserResponseItem>>

    @POST("users")
    fun addUser(@Body request: DataUser): Call<UserResponseItem>
}