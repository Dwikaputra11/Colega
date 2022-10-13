package com.example.colega.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL_NEWS = "https://newsapi.org/v2/"
    private const val BASE_URL_USER = "https://6347ce8d0484786c6e892194.mockapi.io/colega/v1/"

    val instanceFilm: RestfulApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_NEWS)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(RestfulApi::class.java)
    }

    val instanceUser: RestfulApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_USER)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(RestfulApi::class.java)
    }

}