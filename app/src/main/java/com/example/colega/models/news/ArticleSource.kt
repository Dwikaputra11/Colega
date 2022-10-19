package com.example.colega.models.news


import com.google.gson.annotations.SerializedName

data class ArticleSource(
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name: String?
)