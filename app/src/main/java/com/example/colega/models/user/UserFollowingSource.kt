package com.example.colega.models.user


import com.google.gson.annotations.SerializedName

data class UserFollowingSource(
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("sourceId")
    val sourceId: String
)