package com.example.colega.api

import retrofit2.Retrofit

object RetrofitClient {

    const val BASE_URL = "https://newsapi.org/v2/"

    val instance: RestfulApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .build()
        retrofit.create(RestfulApi::class.java)
    }

}