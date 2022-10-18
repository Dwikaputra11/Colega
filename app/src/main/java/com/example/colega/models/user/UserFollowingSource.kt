package com.example.colega.models.user


import com.google.gson.annotations.SerializedName
import org.intellij.lang.annotations.Language

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
    val sourceId: String,
    @SerializedName("language")
    val language: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("description")
    val description: String
)