package com.example.colega.models.user


import com.google.gson.annotations.SerializedName

data class UserResponseItem(
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("birthDate")
    val birthDate: String,
    @SerializedName("bookmarks")
    val bookmarks: List<UserBookmark>,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("followingSource")
    val userFollowingSource: List<UserFollowingSource>,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("username")
    val username: String,
)