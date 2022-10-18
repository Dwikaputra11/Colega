package com.example.colega.api

import com.example.colega.data.users.Bookmark
import com.example.colega.models.user.DataUser
import com.example.colega.models.user.DataFollowingSource
import com.example.colega.models.news.NewsModel
import com.example.colega.models.news.SourceResponse
import com.example.colega.models.user.UserFollowingSource
import com.example.colega.models.user.UserBookmark
import com.example.colega.models.user.UserResponseItem
import com.example.colega.utils.Utils
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
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

    @GET("top-headlines/sources")
    fun getAllSourceNews(@Query("apiKey") apiKey : String = Utils.apiKey): Call<SourceResponse>

//    @GET("top-headlines/")
//    fun getAFewNewsBySource(
//        @Query("apiKey") apiKey: String = Utils.apiKey,
//        @Query("sources") sourceId: String,
//        @Query("pageSize") pageSize: Int = 2
//    )

    @GET("top-headlines/")
    fun getAllNewsBySource(
        @Query("apiKey") apiKey : String = Utils.apiKey,
        @Query("sources") sourceId: String,
    ): Call<NewsModel>


    // USER
    @GET("users")
    fun getAllUsers(): Call<List<UserResponseItem>>

    @GET("users/{id}")
    fun getUser(@Path("id") id: String): Call<UserResponseItem>

    @POST("users")
    fun addUser(@Body request: DataUser): Call<UserResponseItem>

    @PUT("users/{id}")
    fun updateUser(@Path("id")id: String, @Body request: DataUser): Call<UserResponseItem>

    // BOOKMARK
    @GET("users/{id}/bookmark")
    fun getUserBookmark(@Path("id") id: String): Call<List<UserBookmark>>

    @POST("users/{id}/bookmark")
    fun postBookmark(@Path("id") id: String, @Body bookmark: Bookmark): Call<UserBookmark>

    @DELETE("users/{userId}/bookmark/{id}")
    fun deleteBookmark(@Path("userId") userId: String, @Path("id") id: String): Call<UserBookmark>

    // FOLLOWING SOURCE
    @GET("users/{userId}/followingSource")
    fun getUserFollowingSource(@Path("userId") id: String): Call<List<UserFollowingSource>>

    @GET("users/{userId}/followingSource")
    fun getSingleSource(@Path("userId") userId: String,@Query("sourceId") sourceId: String): Call<List<UserFollowingSource>>

    @POST("users/{userId}/followingSource")
    fun postFollowingSource(@Path("userId") id: String, @Body sources: DataFollowingSource): Call<UserFollowingSource>

    @DELETE("users/{userId}/followingSource/{id}")
    fun deleteFollowingSource(@Path("userId") userId: String, @Path("id") id: String): Call<UserFollowingSource>

}