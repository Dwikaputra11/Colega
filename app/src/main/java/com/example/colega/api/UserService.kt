package com.example.colega.api

import com.example.colega.data.users.Bookmark
import com.example.colega.models.user.*
import retrofit2.Call
import retrofit2.http.*

interface UserService {
    // USER
    @GET("users")
    fun getUserByUsername(
        @Query("username") username: String
    ): Call<List<UserResponseItem>>

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
    fun getSingleSource(@Path("userId") userId: String, @Query("sourceId") sourceId: String): Call<List<UserFollowingSource>>

    @POST("users/{userId}/followingSource")
    fun postFollowingSource(@Path("userId") id: String, @Body sources: DataFollowingSource): Call<UserFollowingSource>

    @DELETE("users/{userId}/followingSource/{id}")
    fun deleteFollowingSource(@Path("userId") userId: String, @Path("id") id: String): Call<UserFollowingSource>
}